package de.jwi.servletutil;

/*
 * jGallery - Java Web File Manager
 * 
 * Copyright (C) 2004 Juergen Weber
 * 
 * This file is part of jGallery.
 * 
 * jGallery is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jGallery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jGallery; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;


/**
 * @author Juergen Weber 
 * Source file created on 20.04.2004
 *  
 */
public class PathHelper
{


    public static RealPath getHttpRealPath(ServletContext servletContext, String path,Properties dirmapping, List traceHints) throws IOException
    {
        String s, key;

        
        // check if path is of the form /x/.. and in a mapping

        int p = path.indexOf('/', 1);
        if (p > -1)
        {
        	key = path.substring(1, p);

            s = dirmapping.getProperty(key);
            traceHints.add("dirmapping: " + dirmapping + "key: " + key + " got: " +s);

            if (s != null)
            {
                s = s + path.substring(p);
                File f = new File(s);
                
                traceHints.add("File " + s + " exists: "+f.exists());
                
                if (f.exists()) { 
                	return new RealPath(RealPath.ISHTTPPATH, f.getPath(),
                        s); }
            }
        }

        // check if there is a "/= .. " mapping

        s = dirmapping.getProperty("/");

        if (s != null)
        {
            s = s + path;
            File f = new File(s);
            traceHints.add("File " + s + " exists: "+f.exists());
            if (f.exists()) { return new RealPath(RealPath.ISHTTPPATH, f.getPath(),
                    s); }
        }
        
        
        // no mapping, so try to get a context

        traceHints.add("checking for context");
        
        // first check, if the path is below jGallery's own context
        
        URL url = servletContext.getResource(path);
        
		s = servletContext.getRealPath(path);
		
		if ((url!=null) && (s!=null))
		{
	        return new RealPath(RealPath.ISHTTPPATH, s, servletContext.getServletContextName());
		}
        
        
        ServletContext rootContext = servletContext.getContext("/");

        if (null != rootContext)
        {

            // Work around for Tomcat bug: use only the potential context part of
            // folderPath for getContext

            p = path.indexOf('/', 1);

            s = p > 0 ? path.substring(0, p) : path;

            ServletContext context = servletContext.getContext(s);

            String relativePath = path;

            String rootContextRealPath = rootContext.getRealPath("/");

            String contextName = "/";
            String contextRealPath = context.getRealPath("/");

            if (!(contextRealPath.equals(rootContextRealPath)))
            {
                // not found in ROOT context, so folderPath is of the form
                // /context/path
                // take only /path as input for getRealPath

                relativePath = path.substring(path.indexOf('/', 1));
                contextName = path.substring(0, path.indexOf('/', 1));
            }

            String realPath = null;
            if (null != context)
            {
                realPath = context.getRealPath(relativePath);

                if (!(new File(realPath).exists())) { return null; }

                if ("/".equals(contextName))
                {
                    contextName="";
                }
                
                return new RealPath(RealPath.ISHTTPPATH, realPath, contextName);
            }
            return null;
        }
        else
        {
            // try to get a / mapping

            s = dirmapping.getProperty("/");
            if (s != null)
            {
                s = s + path;
                File f = new File(s);
                if (f.exists()) { return new RealPath(RealPath.ISHTTPPATH, f.getPath(),
                        s); }
            }
        }
        return null;
    }

    public static RealPath getFileRealPath(String filebase,String path) throws IOException
    {
        String s = filebase;
    
        if (null != s)
        {
            if (!s.endsWith("/"))
            {
                s = s + "/";
            }
            s = s + path;
        }
        else
        {
            s = path;
        }
    
        File f = new File(s);
    
        if (!(f.exists())) { return null; }
    
        return new RealPath(false, f.getPath(), null);
    
    }

}