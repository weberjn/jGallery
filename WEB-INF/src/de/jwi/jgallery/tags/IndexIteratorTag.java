package de.jwi.jgallery.tags;

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

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import de.jwi.jgallery.Folder;

/**
 * @author Juergen Weber 
 * Source file created on 10.03.2004
 *  
 */
public class IndexIteratorTag extends BodyTagSupport
{

    public static final String CURRENTINDEX = "index";

    private int totalIndexes;

    private int index;

    private Folder folder;

    private IndexIteratorCursor cursor;

    private void updateCursor()
    {
        int i = totalIndexes - index + 1;

        String page = folder.getCalculatedIndexPage(i);
        String number = Integer.toString(i);
        String selected = folder.getIndexNum().equals(number) ? "selected" : "";

        cursor.setPage(page);
        cursor.setNumber(number);
        cursor.setSelected(selected);
    }

    public int doStartTag() throws JspTagException
    {
        folder = (Folder) pageContext.getRequest().getAttribute("folder");

        if (null == folder) { throw new JspTagException(
                "no folder Attribute found in request"); }

        int i = 1;
        try
        {
            i = Integer.parseInt(folder.getTotalIndexes());
        }
        catch (NumberFormatException e1)
        {
            // empty String, leave preset 1
        }
        index = totalIndexes = i;

        if (index > 0)
        {
            cursor = new IndexIteratorCursor();
            
            updateCursor();
            pageContext.setAttribute(CURRENTINDEX, cursor);

            index--;

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
        if (index > 0)
        {
            updateCursor();
            pageContext.setAttribute(CURRENTINDEX, cursor);

            index--;

            return (EVAL_BODY_AGAIN);
        }
        else
        {
            return SKIP_BODY;
        }
    }
}
