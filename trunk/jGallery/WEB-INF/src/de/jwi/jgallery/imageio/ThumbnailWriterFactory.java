package de.jwi.jgallery.imageio;

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

/**
 * @author Jürgen Weber
 * Source file created on 29.02.2004
 *  
 */
public class ThumbnailWriterFactory
{
    private static ThumbnailWriterFactory instance = new ThumbnailWriterFactory();
    
    private ThumbnailWriterFactory()
    {
        
    }

    public static ThumbnailWriterFactory getInstance()
    {
        return instance;
    }
    
     
    public ThumbnailWriter createThumbnailWriter() 
    {
        return new ImageIOThumbnailWriter();
    }
}
