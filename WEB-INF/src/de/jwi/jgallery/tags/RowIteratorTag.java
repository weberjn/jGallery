package de.jwi.jgallery.tags;

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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import de.jwi.jgallery.Folder;
import de.jwi.jgallery.GalleryException;
import de.jwi.jgallery.Image;

/**
 * @author Juergen Weber
 * Source file created on 18.02.2004
 *
 * @see http://java.sun.com/products/jsp/tutorial/TagLibraries16.html  
 */
public class RowIteratorTag extends BodyTagSupport
{

    private int cols;

    private int imagesLeft;

    private int currentImageNum;

    private Folder folder;

    // this is a callback for ColIteratorTag
    int getColsLeft()
    {
        int n = Math.min(imagesLeft, cols);
        imagesLeft -= n;
        return n;
    }

    //  this is a callback for ColIteratorTag
    Image getNextImage()
    throws GalleryException
    {
        return folder.getSubDirOrImage(currentImageNum++);
    }

    public int doStartTag() throws JspException
    {
        folder = (Folder) pageContext.getRequest().getAttribute(
                "folder");

        if (null == folder) { throw new JspException(
                "no folder Attribute found in request"); }

        imagesLeft = folder.getCurrentImagesPerPage();

        if (imagesLeft > 0)
        {
            cols = folder.getColsI();
            currentImageNum = folder.getImageNumI();

            return (EVAL_BODY_AGAIN);
        }
        else
        {
            return SKIP_BODY;
        }
    }

    public int doAfterBody() throws JspTagException
    {
        BodyContent body = getBodyContent();
        try
        {
            body.writeOut(getPreviousOut());
        }
        catch (IOException e)
        {
            throw new JspTagException("IterationTag: " + e.getMessage());
        }

        body.clearBody();
        if (imagesLeft > 0)
        {
            return EVAL_BODY_AGAIN;
        }
        else
        {
            return SKIP_BODY;
        }

    }
}
