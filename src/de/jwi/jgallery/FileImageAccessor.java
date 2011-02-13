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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Jürgen Weber
 * Source file created on 07.03.2004
 *  
 */
public class FileImageAccessor implements IImageAccessor
{

    private String name;

    private Folder folder;

    private File image;
    
    InputStream imageInputStream;
    InputStream thumbInputStream;
    File directory;

    FileImageAccessor(String name, Folder folder)
    {
        this.name = name;
        this.folder = folder;
    }

    private File getFile()
    {
        if (image == null)
        {
            directory = folder.getDirectory();

            image = new File(directory, name);
        }
        return image;
    }
    
    public InputStream getImageInputStream() 
    throws FileNotFoundException
    {
        imageInputStream = new FileInputStream(getFile());
        return imageInputStream;
    }

    public long getLastModified()
    {
        return getFile().lastModified();
    }

    public long getLength()
    {
        return getFile().length();
    }
    
    public InputStream getThumbInputStream() 
    throws FileNotFoundException
    {
        if (null==thumbInputStream)
        {
            getFile(); // to trigger directory
            
            File thumb = new File(directory,
                    folder.getThumbsdir() + "/" + name);
            
            thumbInputStream = new FileInputStream(thumb);
        }
        return thumbInputStream;
    }
}
