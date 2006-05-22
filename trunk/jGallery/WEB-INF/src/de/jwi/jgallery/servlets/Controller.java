
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
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

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
import de.jwi.jgallery.IThumbnailWriter;
import de.jwi.jgallery.WebFolder;
import de.jwi.jgallery.db.DBManager;
import de.jwi.servletutil.PathHelper;
import de.jwi.servletutil.RealPath;

/**
 * @author Jürgen Weber Source file created on 17.02.2004
 *  
 */
public class Controller extends HttpServlet
{

	private static String version = "unknown";

	private static String urlExtention;

	private static final String CONFIGFILE = "jGallery.properties";

	static final String VERSIONCONFIGFILE = "version.properties";

	private static final String WEBDIRSFILE = "web.properties";

	private Configuration configuration;

	private Properties webDirectories = null;

	private HashSet webKeys = new HashSet();

	private Properties dirmapping = null;

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
			}
			catch (NamingException e)
			{
				throw new ServletException(e.getMessage(), e);
			}
		}
	}


	public void init() throws ServletException
	{
		ServletContext context = getServletContext();

		dataSource = context.getInitParameter("dataSource");

		String s = context.getInitParameter("dirmappings");

		dirmapping = new Properties();

		if (null != s)
		{
			StringTokenizer st = new StringTokenizer(s, ",");
			while (st.hasMoreTokens())
			{
				String s1 = st.nextToken();
				int p = s1.indexOf('=');
				if (p > -1)
				{
					String key = s1.substring(0, p);
					String val = s1.substring(p + 1);
					dirmapping.setProperty(key, val);
				}
			}
		}

		s = context.getInitParameter("useDataBase");

		if (null != s)
		{
			useDataBase = Boolean.valueOf(s).booleanValue();
			initDBConnection();
		}

		InputStream is = context.getResourceAsStream("/WEB-INF/" + CONFIGFILE);

		try
		{
			configuration = new Configuration(is);

			s = context.getInitParameter("thumbnailWriter");

			Object o = Class.forName(s).newInstance();

			configuration.setThumbnailWriter((IThumbnailWriter) o);
		}
		catch (Exception e)
		{
			throw new ServletException(e.getMessage());
		}

		if (is != null)
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				throw new ServletException(e.getMessage());
			}
		}


		// Parameters from the config file in WEB-INF can be overridden from parameters 
		// in the context configuration

		Configuration configurationFromContext = null;
		Enumeration en = context.getInitParameterNames();
		while (en.hasMoreElements())
		{
			if (configurationFromContext == null)
			{
				configuration = configurationFromContext = new Configuration(
						configuration);
			}
			s = (String) en.nextElement();
			String s1 = context.getInitParameter(s);
			configurationFromContext.addProperty(s, s1);
		}


		is = context.getResourceAsStream("/WEB-INF/" + WEBDIRSFILE);

		if (null != is)
		{
			try
			{
				webDirectories = new Properties();
				webDirectories.load(is);

				for (en = webDirectories.keys(); en.hasMoreElements();)
				{
					webKeys.add(en.nextElement());
				}

			}
			catch (IOException e)
			{
				throw new ServletException(e.getMessage());
			}
		}

		if (is != null)
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				throw new ServletException(e.getMessage());
			}
		}

		String versioncfg = "/WEB-INF/" + VERSIONCONFIGFILE;
		try
		{
			is = context.getResourceAsStream(versioncfg);

			Properties versionProperties = new Properties();
			versionProperties.load(is);

			s = versionProperties.getProperty("version");
			if (null != s)
			{
				version = s;
			}
		}
		catch (Exception e)
		{
			throw new ServletException(versioncfg + " not found");
		}

		if (is != null)
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				throw new ServletException(e.getMessage());
			}
		}

	}

	public static String getVersion()
	{
		return version;
	}


	private void rememberFolder(HttpSession session, Folder folder,
			String folderPath)
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


	private Folder createFolder(HttpSession session, String folderPath,
			String imagePath, String folderRealPath,
			String jgalleryContextPath, boolean doCount)
			throws GalleryException
	{
		// No need to synchronize access to the Folders because they are per
		// session. In the worst case if a user has several browser windows
		// (that share the same session), there might be unnecessary creations of
		// Folders.

		Folder folder;
		Configuration configuration = this.configuration;

		File directory = new File(folderRealPath);

		if (!directory.exists())
		{
			throw new GalleryNotFoundException("directory does not exist: "
					+ directory.toString());
		}

		File config = new File(directory, CONFIGFILE);
		if (config.exists())
		{
			InputStream is;
			try
			{
				is = new FileInputStream(config);
			}
			catch (FileNotFoundException e)
			{
				throw new GalleryException(e.getMessage());
			}

			try
			{
				Configuration conf = new Configuration(is, configuration);
				configuration = conf;
			}
			catch (IOException e)
			{
				throw new GalleryException(e.getMessage());
			}
			finally
			{
				if (is != null)
				{
					try
					{
						is.close();
					}
					catch (IOException e)
					{
						throw new GalleryException(e.getMessage());
					}
				}
			}
		}

		ConfigData configData = new ConfigData();
		configData.version = version;
		configData.urlExtention = urlExtention;
		configData.doCount = doCount;

		folder = new Folder(directory, getServletContext(), configuration,
				configData, jgalleryContextPath, folderPath, imagePath,
				dBManager);

		return folder;
	}

	private Folder createWebFolder(HttpSession session, String remoteKey,
			String folderPath, String baseURL, String jgalleryContextPath)
			throws GalleryException
	{
		// No need to synchronize access to the Folders because they are per
		// session. In the worst case if a user has several browser windows
		// (that
		// share the same session), there might be unnecessary creations of
		// Folders.

		Configuration configuration = this.configuration;
		Folder folder;
		InputStream wis = null;
		URL url;
		URLConnection connection = null;
		try
		{
			url = new URL(baseURL + folderPath + "images.txt");
			connection = url.openConnection();
			connection.connect();
		}
		catch (Exception e)
		{
			throw new GalleryException(e.getMessage());
		}

		try
		{
			wis = connection.getInputStream();
			long l = connection.getLastModified();
		}
		catch (IOException e)
		{
			throw new GalleryNotFoundException(e.getMessage());
		}

		try
		{
			URL url1 = new URL(baseURL + folderPath + CONFIGFILE);
			URLConnection connection1 = url1.openConnection();
			connection1.connect();
			InputStream is = connection1.getInputStream();

			if (null != is)
			{
				Configuration conf = new Configuration(is, configuration);
				configuration = conf;
			}
		}
		catch (Exception e1)
		{
			// nothing
		}
		configuration = new Configuration(configuration);
		configuration.addProperty("thumbnails.create", "false");

		ConfigData configData = new ConfigData();
		configData.version = version;
		configData.urlExtention = urlExtention;

		folder = new WebFolder(baseURL, getServletContext(), configuration,
				configData, remoteKey, jgalleryContextPath, folderPath, wis);

		return folder;
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
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

		String folderPath = servletPath.substring(0, servletPath
				.lastIndexOf('/') + 1);
		String imageName = servletPath
				.substring(servletPath.lastIndexOf('/') + 1);

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
				RealPath theRealPath = PathHelper.getHttpRealPath(
						getServletContext(), folderPath, dirmapping);

				if (null == theRealPath)
				{
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
							request.getRequestURI());
					return;
				}

				folderRealPath = theRealPath.getRealPath();

				String imagePath = folderPath;

				if (!"".equals(contextPath) && contextPath.substring(1).equals(theRealPath.getContext()))
				{
					// special case for image folders below jGallery's context

					imagePath = contextPath + folderPath;
				}

				folder = createFolder(session, folderPath,
						imagePath, folderRealPath, contextPath, doCount);

				folder.loadFolder();
				
				rememberFolder(session, folder,folderPath);
			}
			int n = folder.setFileName(imageName);

			if (Folder.INDEX == n)
			{
				forward = folder.getIndexJsp();
			}
			else
			{
				forward = folder.getSlideJsp();
			}

			request.setAttribute("folder", folder);
			request.setAttribute("image", folder.getImage());

		}
		catch (GalleryNotFoundException e)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND, request
					.getRequestURI());
			return;
		}
		catch (GalleryException e)
		{
			e.printStackTrace(System.err);
			throw new ServletException(e.getMessage());
			//            request.setAttribute("javax.servlet.error.exception",e);
			//          forward = "/errorpage.jsp";
		}

		RequestDispatcher requestDispatcher = getServletContext()
				.getRequestDispatcher(forward);

		requestDispatcher.forward(request, response);
	}
}