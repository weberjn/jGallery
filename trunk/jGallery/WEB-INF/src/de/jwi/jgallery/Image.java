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

import imageinfo.ImageInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Jürgen Weber
 * Source file created on 22.02.2004 
 */
public class Image implements Serializable
{

    private boolean isRepresentsSubdirectory = false;

    private String name;

    private Folder folder;

    private String closeupPath = "";

    private String fileName = "";

    private String fileSize = "";

    private String imagePath = "";

    private String thumbPath = "";

    private String thumbWidth;

    private String thumbHeight;

    private String fileDate = "";
    private long lastModified;

    private String imageWidth; //	Width of image in Pixels

    private String imageHeight; //	Height of image in pixels

    private String comment;

    private EXIFInfo exif;

    Image(String name, boolean isRepresentsSubdirectory, Folder folder,
            IImageAccessor imageAccessor) throws GalleryException
    {
        this.name = name;
        this.folder = folder;
        this.isRepresentsSubdirectory = isRepresentsSubdirectory;

        lastModified = imageAccessor.getLastModified();

        setFileDate(DateFormat.getDateInstance().format(new Date(lastModified)));

        InputStream imageInputStream;

        if (!isRepresentsSubdirectory)
        {
            try
            {
                // get image size using ImageInfo class

                imageInputStream = imageAccessor.getImageInputStream();
                ImageInfo ii = new ImageInfo();
                ii.setInput(imageInputStream);
                if (!ii.check()) { throw new GalleryException(
                        "Not a supported image file format."); }

                imageWidth = Integer.toString(ii.getWidth());
                imageHeight = Integer.toString(ii.getHeight());

                fileSize = Long.toString(imageAccessor.getLength());

                //              get thumbnail image size using ImageInfo class

                InputStream thumbInputStream = imageAccessor
                        .getThumbInputStream();

                ii = new ImageInfo();
                ii.setInput(thumbInputStream);
                if (!ii.check()) { throw new GalleryException(
                        "Not a supported image file format."); }

                thumbWidth = Integer.toString(ii.getWidth());
                thumbHeight = Integer.toString(ii.getHeight());

            }
            catch (FileNotFoundException e)
            {
                throw new GalleryException(e.getMessage());
            }

            try
            {
                imageInputStream = imageAccessor.getImageInputStream();
            }
            catch (FileNotFoundException e)
            {
                throw new GalleryException(e.getMessage());
            }

            try
            {
                exif = new EXIFInfo(imageInputStream);
            }
            catch (GalleryException e1)
            {
                exif = null;
            }
        }

    }

    public String toString()
    {
        return name;
    }

    public String getName()
    {
        return name;
    }

    private String labelfile;

    public String getLabelfile() throws FileNotFoundException, IOException,
            GalleryException
    {
        if (null == labelfile)
        {
            labelfile = folder.getFileContent(getLabel() + ".txt");
        }

        return labelfile;
    }

    public String getLabel()
    {
        int p = name.indexOf('.');

        return p > -1 ? name.substring(0, p) : name;
    }

    public String getFileName()
    {
        return name;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getCloseupPath()
    {
        String n;
        if (isRepresentsSubdirectory)
        {
            n = name + "/index.html";
        }
        else
        {
            n = name.substring(0, name.indexOf('.')) + ".html";
        }
        return folder.getHTMLBase() + n;
    }

    public String getIconPath()
    {
        return isRepresentsSubdirectory ? folder.getIconPath() : "";
    }

    public String getImagePath()
    {
        return folder.getFolderPath() + "/" + name;
    }

    public String getThumbHeight()
    {
        return thumbHeight;
    }

    public String getThumbPath()
    {
        return folder.getThumbsPath() + "/" + name;
    }

    public void setThumbPath(String thumbPath)
    {
        this.thumbPath = thumbPath;
    }

    public String getThumbWidth()
    {
        return thumbWidth;
    }

    /**
     * @return Path to original file. Only defined if linking is "Link to originals via scaled images". If linking is
     *         "Link to originals" then imagePath links to the original image
     */
    public String getOriginalPath()
    {
        return "";
    }

    public String getImageHeight()
    {
        return imageHeight;
    }

    public String getImageWidth()
    {
        return imageWidth;
    }

    public Folder getFolder()
    {
        return folder;
    }

    public void setFolder(Folder folder)
    {
        this.folder = folder;
    }

    public void setCloseupPath(String closeupPath)
    {
        this.closeupPath = closeupPath;
    }

    public void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public void setImageWidth(String imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFileDate()
    {
        return fileDate;
    }

    public void setFileDate(String fileDate)
    {
        this.fileDate = fileDate;
    }

    public EXIFInfo getExif()
    {
        return exif;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getFileSize()
    {
        return fileSize;
    }
    public long getLastModified()
    {
        return lastModified;
    }
    
    public String getCounter()
    {
        int c = folder.getImageCounter(name);
        
        return c>-1 ? Integer.toString(c) : "";
    }
    
}
