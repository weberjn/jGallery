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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;



/**
 * @author Jürgen Weber
 * Source file created on 07.03.2004
 *
 */
public class WebFolder extends Folder
{
    private String baseURL;
    private String remoteKey;
    
    public WebFolder(String baseURL, ServletContext appContext, Configuration configuration, String version, String remoteKey, String jgalleryContextPath,  
            String folderPath, InputStream fileList) throws GalleryException
    {
        super(null, appContext, configuration,version, jgalleryContextPath,folderPath,folderPath,null);
        this.baseURL = baseURL;
        this.remoteKey = remoteKey;
        
        List images = new ArrayList();
        
        BufferedReader in
        = new BufferedReader(new InputStreamReader(fileList));
        
        String s;
        
        try
        {
            while ((s=in.readLine())!=null)
            {
                images.add(s); 
            }
        }
        catch (IOException e)
        {
            throw new GalleryException(e.getMessage());
        }
        
        imageFiles = (String[])images.toArray(new String[0]);
        
        endLoad();
    }
    
    protected IImageAccessor makeImageAccessor(String name)
    {
        return new WebImageAccessor(name, this);
    }

    public void loadFolder()
    {
        // NOP

    }
    
    protected String[] getSubDirectories()
    {
        return new String[0];
    }
    
    public String getBaseURL()
    {
        return baseURL;
    }
    
    public String getFolderPath()
    {
        return baseURL + folderPath;
    }

    public String getThumbsPath()
    {
        return baseURL + super.getThumbsPath();
    }    
    
    public String getHTMLBase()
    {
        return jgalleryContextPath + "/" + remoteKey  + folderPath;
    }
    
    
}
