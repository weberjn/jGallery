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
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import de.jwi.jgallery.Configuration;
import de.jwi.jgallery.Folder;
import de.jwi.jgallery.GalleryException;
import de.jwi.jgallery.GalleryNotFoundException;
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

    private static final String FOLDERS = "folders";

    private static final String CONFIGURATION = "configuration";

    private static final String CONFIGFILE = "jGallery.properties";

    private static final String VERSIONCONFIGFILE = "version.properties";

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
                if (context == null) { throw new ServletException(
                        "Boom - No Context"); }
                DataSource ds = (DataSource) context
                        .lookup(dataSource);

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
        dataSource = (String) getServletContext().getInitParameter("dataSource");
        
        String s = (String) getServletContext().getInitParameter("dirmappings");

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

        s = getServletContext().getInitParameter("useDataBase");

        if (null != s)
        {
            useDataBase = Boolean.valueOf(s).booleanValue();
            initDBConnection();
        }

        InputStream is = getServletContext().getResourceAsStream(
                "/WEB-INF/" + CONFIGFILE);

        if (null == is) { throw new ServletException(
                "Resource does not exist: " + CONFIGFILE); }
        try
        {
            configuration = new Configuration(is);
        }
        catch (IOException e)
        {
            throw new ServletException(e.getMessage());
        }

        is = getServletContext().getResourceAsStream("/WEB-INF/" + WEBDIRSFILE);

        if (null != is)
        {
            try
            {
                webDirectories = new Properties();
                webDirectories.load(is);

                for (Enumeration e = webDirectories.keys(); e.hasMoreElements();)
                {
                    webKeys.add(e.nextElement());
                }

            }
            catch (IOException e)
            {
                throw new ServletException(e.getMessage());
            }
        }

        try
        {
            is = getServletContext().getResourceAsStream(
                    "/WEB-INF/" + VERSIONCONFIGFILE);

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
            e.printStackTrace(System.err); // TODO
        }
    }

    public static String getVersion()
    {
        return version;
    }

    private Folder getFolder(HttpSession session, String folderPath)
    {
        Hashtable folders = getFolders(session);

        Folder folder = (Folder) folders.get(folderPath);

        return folder;
    }

    private Hashtable getFolders(HttpSession session)
    {
        Hashtable folders = (Hashtable) session.getAttribute(FOLDERS);

        if (null == folders)
        {
            folders = new Hashtable();
            session.setAttribute(FOLDERS, folders);
        }
        return folders;
    }

    private Folder createFolder(HttpSession session, String folderPath,
            String folderRealPath, String jgalleryContextPath)
            throws GalleryException
    {
        // No need to synchronize access to the Folders because they are per
        // session. In the worst case if a user has several browser windows (that
        // share the same session), there might be unnecessary creations of Folders.

        Folder folder;
        Configuration configuration = this.configuration;

        File directory = new File(folderRealPath);

        if (!directory.exists()) { throw new GalleryNotFoundException(
                "directory does not exist: " + directory.toString()); }

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
        }

        folder = new Folder(directory, getServletContext(), configuration, version,
                jgalleryContextPath, folderPath, dBManager);

        Hashtable folders = getFolders(session);

        folders.put(folderPath, folder);

        return folder;
    }

    private Folder createWebFolder(HttpSession session, String remoteKey,
            String folderPath, String baseURL, String jgalleryContextPath)
            throws GalleryException
    {
        // No need to synchronize access to the Folders because they are per
        // session. In the worst case if a user has several browser windows (that
        // share the same session), there might be unnecessary creations of Folders.

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

        folder = new WebFolder(baseURL, getServletContext(), configuration, version, remoteKey,
                jgalleryContextPath, folderPath, wis);

        Hashtable folders = getFolders(session);

        folders.put(folderPath, folder);

        return folder;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");

        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();
        String pathTranslated = request.getPathTranslated();
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();

        // contextPath := "/JGallery"
        String contextPath = request.getContextPath();

        String servletPath = request.getServletPath();

        // servletPath := "/galleries" with path mapping

        // servletPath= "/testalbum/CRW_9753.html" with ext mapping

        // /myfolder/subfolder/myimage.html
        // gets split into /myfolder/subfolder/ (which is used as key for the Folder object)
        // and imageName myimage.html

        String folderPath = servletPath.substring(0, servletPath
                .lastIndexOf('/') + 1);
        String imageName = servletPath
                .substring(servletPath.lastIndexOf('/') + 1);

        // getServletContext().log("folderPath: "+folderPath);
        
        
        
        String folderRealPath = null;

        String forward = null;

        Folder folder = getFolder(request.getSession(), folderPath);

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

                folder = createFolder(request.getSession(), folderPath,
                        folderRealPath, contextPath);

                folder.loadFolder();
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
            throw new ServletException(e.getMessage());
            //            request.setAttribute("javax.servlet.error.exception",e);
            //          forward = "/errorpage.jsp";
        }

        RequestDispatcher requestDispatcher = getServletContext()
                .getRequestDispatcher(forward);

        requestDispatcher.forward(request, response);
    }
}