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

/**
 * @author Jürgen Weber
 * Source file created on 28.02.2004
 *  
 */
public class DirectoriesFilter implements java.io.FilenameFilter
{
    private String thumbsDirName;
    
    public DirectoriesFilter(String thumbsDirName)
    {
        this.thumbsDirName=thumbsDirName;
    }
    public boolean accept(File dir, String name)
    {
        if (thumbsDirName.equals(name)) return false;
        
        return (new File(dir, name).isDirectory());
    }
}
