package de.jwi.jgallery;

/*
 * jGallery - Java web application to display image galleries
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jürgen Weber
 * Source file created on 07.03.2004
 *  
 */
public class WebImageAccessor implements ImageAccessor
{

    private String name;

    private WebFolder folder;

    InputStream imageInputStream;
    InputStream thumbInputStream;
    File directory;
    
    private long lastModified;
    private long length;
    
    private String baseURL;
    private String folderPath;

    WebImageAccessor(String name, WebFolder folder)
    {
        this.name = name;
        this.folder = folder;
        baseURL = folder.getBaseURL();
        folderPath = folder.getFolderPath();
    }

    
    public InputStream getImageInputStream() 
    throws FileNotFoundException
    {
        
        URL url;
        URLConnection connection;
        
        try
        {
            url = new URL(folderPath + "/" + name);
            connection = url.openConnection();
            connection.connect();
        }
        catch (Exception e)
        {
            throw new FileNotFoundException(e.getMessage());
        }
        
        try
        {
            imageInputStream = connection.getInputStream();
        }
        catch (IOException e)
        {
            throw new FileNotFoundException(e.getMessage());
        }
        
        lastModified = connection.getLastModified();   
        length = connection.getContentLength();
        
        return imageInputStream;
    }

    public long getLastModified()
    {
        return lastModified;
    }

    public long getLength()
    {
        return length;
    }
    
    
    public InputStream getThumbInputStream() 
    throws FileNotFoundException
    {
        
        URL url;
        URLConnection connection;
        
        try
        {
            url = new URL(folderPath + folder.getThumbsdir() + "/" + name);
            connection = url.openConnection();
            connection.connect();
        }
        catch (Exception e)
        {
            throw new FileNotFoundException(e.getMessage());
        }
        
        try
        {
            imageInputStream = connection.getInputStream();
        }
        catch (IOException e)
        {
            throw new FileNotFoundException(e.getMessage());
        }
        
        lastModified = connection.getLastModified();   
        length = connection.getContentLength();
        
        return imageInputStream;
    }    
    
    
}
