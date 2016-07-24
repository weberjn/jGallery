
package de.jwi.jgallery.servlets;

/*
 * jGallery - Java web application to display image galleries
 * 
 * Copyright (C) 2004 Juergen Weber
 * 
 * This file is part of jGallery.
 * 
 * jGallery is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * jGallery is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with jGallery; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import de.jwi.jgallery.ConfigData;
import de.jwi.jgallery.Configuration;
import de.jwi.jgallery.Folder;
import de.jwi.jgallery.GalleryException;
import de.jwi.jgallery.GalleryNotFoundException;
import de.jwi.jgallery.db.DBManager;
import de.jwi.jgallery.imageio.ImageIOThumbnailWriter;
import de.jwi.servletutil.PathHelper;
import de.jwi.servletutil.RealPath;

/**
 * @author Jürgen Weber Source file created on 17.02.2004
 * 
 */
public class Controller extends HttpServlet
{

	private static String version = "unknown";

	private static String builddate = "unknown";

	private static String urlExtention;

	private static final String CONFIGFILE = "/jGallery.properties";

	static final String VERSIONCONFIGFILE = "/version.properties";

	private Configuration configuration;

	private Properties dirmappings = null;

	private String dataSource = null;

	private boolean useDataBase = false;

	private DBManager dBManager;

	private void initDBConnection() throws ServletException
	{
		if (useDataBase)
		{
			Context context;
			try
			{
				context = new InitialContext();
				if (context == null)
				{
					throw new ServletException("Boom - No Context");
				}
				DataSource ds = (DataSource) context.lookup(dataSource);

				dBManager = new DBManager(ds);
			} catch (NamingException e)
			{
				throw new ServletException(e.getMessage(), e);
			}
		}
	}

	public void init() throws ServletException
	{
		ServletContext context = getServletContext();

		Properties propsCP = new Properties();
		Properties propsWI = new Properties();

		InputStream is = null;

		URL urlWI = null;

		try
		{
			urlWI = context.getResource("/WEB-INF" + CONFIGFILE);
			if (urlWI != null)
			{
				context.log("reading " + urlWI);
				is = context.getResourceAsStream("/WEB-INF" + CONFIGFILE);

				propsWI.load(is);
				is.close();
			}
		} catch (IOException e)
		{
			throw new ServletException(e);
		}

		URL urlCP = getClass().getResource(CONFIGFILE);
		if (urlCP != null)
		{
			context.log("reading " + urlCP);
			is = getClass().getResourceAsStream(CONFIGFILE);
			try
			{
				propsCP.load(is);
				is.close();
			} catch (IOException e)
			{
				throw new ServletException(e);
			}
		}

		if (urlWI == null && urlCP == null)
		{
			context.log("no " + CONFIGFILE + " found");
		}

		// Classpath properties override WEB-INF properties
		propsWI.putAll(propsCP);

		dataSource = propsWI.getProperty("dataSource");

		dirmappings = new Properties();

		for (Enumeration e = propsWI.propertyNames(); e.hasMoreElements();)
		{
			String s = (String) e.nextElement();

			// gallery.holiday=D:/holiday

			if (s.startsWith("gallery."))
			{
				String key = s.substring("gallery.".length());
				String val = propsWI.getProperty(s);
				dirmappings.setProperty(key, val);
			}
		}

		String s = propsWI.getProperty("useDataBase");

		if (null != s)
		{
			useDataBase = Boolean.valueOf(s).booleanValue();
			initDBConnection();
		}

		configuration = new Configuration(propsWI);

		configuration.setThumbnailWriter(new ImageIOThumbnailWriter());

		is = Controller.class.getResourceAsStream(VERSIONCONFIGFILE);
		if (is == null)
		{
			throw new ServletException(VERSIONCONFIGFILE + " not found");
		}

		Properties versionProperties = new Properties();
		try
		{
			versionProperties.load(is);
			is.close();
		} catch (IOException e)
		{
			throw new ServletException(e);
		}

		s = versionProperties.getProperty("version");
		version = s;

		s = versionProperties.getProperty("builddate");
		builddate = s;
	}

	public static String getVersion()
	{
		return version;
	}

	public static String getBuilddate()
	{
		return builddate;
	}

	private void rememberFolder(HttpSession session, Folder folder, String folderPath)
	{
		session.setAttribute("lastFolder", folder);
		session.setAttribute("lastFolderPath", folderPath);
	}

	private Folder getFolder(HttpSession session, String folderPath)
	{
		Folder f = null;

		String lastFolderPath = (String) session.getAttribute("lastFolderPath");
		Folder lastFolder = (Folder) session.getAttribute("lastFolder");

		if ((lastFolder != null) && folderPath.equals(lastFolderPath))
		{
			f = lastFolder;
		}

		return f;
	}

	private Folder createFolder(HttpSession session, String folderPath, String imagePath, String folderRealPath,
			String jgalleryContextPath, boolean doCount) throws GalleryException
	{
		// No need to synchronize access to the Folders because they are per
		// session. In the worst case if a user has several browser windows
		// (that share the same session), there might be unnecessary creations
		// of
		// Folders.

		Folder folder;
		Configuration configuration = this.configuration;

		File directory = new File(folderRealPath);

		if (!directory.exists())
		{
			throw new GalleryNotFoundException("directory does not exist: " + directory.toString());
		}

		File config = new File(directory, CONFIGFILE);
		if (config.exists())
		{
			InputStream is;
			try
			{
				is = new FileInputStream(config);
			} catch (FileNotFoundException e)
			{
				throw new GalleryException(e.getMessage());
			}

			try
			{
				Configuration conf = new Configuration(is, configuration);
				configuration = conf;
			} catch (IOException e)
			{
				throw new GalleryException(e.getMessage());
			} finally
			{
				if (is != null)
				{
					try
					{
						is.close();
					} catch (IOException e)
					{
						throw new GalleryException(e.getMessage());
					}
				}
			}
		}

		ConfigData configData = new ConfigData();
		configData.version = version;
		configData.builddate = builddate;
		
		configData.urlExtention = urlExtention;
		configData.doCount = doCount;

		folder = new Folder(directory, getServletContext(), configuration, configData, jgalleryContextPath, folderPath,
				imagePath, dBManager);

		return folder;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();
		String pathTranslated = request.getPathTranslated();
		String requestURI = request.getRequestURI();
		String requestURL = request.getRequestURL().toString();

		// contextPath := "/jGallery"
		String contextPath = request.getContextPath();

		String servletPath = request.getServletPath();

		String nocount = request.getParameter("nocount");
		boolean doCount = (!"true".equals(nocount));

		// servletPath := "/galleries" with path mapping

		// servletPath= "/testalbum/CRW_9753.html" with ext mapping

		// /myfolder/subfolder/myimage.html
		// gets split into /myfolder/subfolder/ (which is used as key for the
		// Folder object)
		// and imageName myimage.html

		String folderPath = servletPath.substring(0, servletPath.lastIndexOf('/') + 1);
		String imageName = servletPath.substring(servletPath.lastIndexOf('/') + 1);

		urlExtention = imageName.substring(imageName.lastIndexOf('.') + 1);

		// getServletContext().log("folderPath: "+folderPath);

		String folderRealPath = null;

		String forward = null;

		HttpSession session = request.getSession();

		Folder folder = getFolder(session, folderPath);

		try
		{
			if (null == folder)
			{
				List traceHints = new ArrayList();
				RealPath theRealPath = PathHelper.getHttpRealPath(getServletContext(), folderPath, dirmappings,
						traceHints);

				if (null == theRealPath)
				{
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "path: " + folderPath + ": " + traceHints);
					return;
				}

				folderRealPath = theRealPath.getRealPath();

				String imagePath = folderPath;

				if (!"".equals(contextPath) && contextPath.substring(1).equals(theRealPath.getContext()))
				{
					// special case for image folders below jGallery's context

					imagePath = contextPath + folderPath;
				}

				folder = createFolder(session, folderPath, imagePath, folderRealPath, contextPath, doCount);

				folder.loadFolder();

				rememberFolder(session, folder, folderPath);
			}
			int n = folder.setFileName(imageName);

			if (Folder.INDEX == n)
			{
				forward = folder.getIndexJsp();
			} else
			{
				forward = folder.getSlideJsp();
			}

			request.setAttribute("folder", folder);
			request.setAttribute("image", folder.getImage());

		} catch (GalleryNotFoundException e)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
			return;
		} catch (GalleryException e)
		{
			e.printStackTrace(System.err);
			throw new ServletException(e.getMessage(), e);
			// request.setAttribute("javax.servlet.error.exception",e);
			// forward = "/errorpage.jsp";
		}

		RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(forward);

		requestDispatcher.forward(request, response);
	}
}