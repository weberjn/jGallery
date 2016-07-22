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

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import de.jwi.jgallery.GalleryException;
import de.jwi.jgallery.Image;

/**
 * @author Juergen Weber
 * Source file created on 18.02.2004
 *  
 */
public class ColIteratorTag extends BodyTagSupport
{

    public static final String CURRENTIMAGE = "image";

    private int colsLeft;

    private RowIteratorTag rowIteratorTag;

    public int doStartTag() throws JspTagException
    {
        Tag parent = getParent();

        if ((null == parent) || (!(parent instanceof RowIteratorTag))) { throw new JspTagException(
                "colIterator tag not within rowIterator tag"); }
        rowIteratorTag = (RowIteratorTag) parent;
        colsLeft = rowIteratorTag.getColsLeft();
        if (colsLeft > 0)
        {
            Image currentImage = null;
            colsLeft--;

            try
            {
                currentImage = rowIteratorTag.getNextImage();
                pageContext.getRequest().setAttribute(CURRENTIMAGE, currentImage);
            }
            catch (GalleryException e)
            {
                throw new JspTagException(e.getMessage());
            }

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

        // clear up so the next time the body content is empty
        body.clearBody();
        if (colsLeft > 0)
        {
            colsLeft--;
            try
            {
                Image currentImage = rowIteratorTag.getNextImage();
                pageContext.getRequest().setAttribute(CURRENTIMAGE,
                        currentImage);
            }
            catch (GalleryException e)
            {
                throw new JspTagException(e.getMessage());
            }
            return (EVAL_BODY_AGAIN);
        }
        else
        {
            return SKIP_BODY;
        }
    }
}
