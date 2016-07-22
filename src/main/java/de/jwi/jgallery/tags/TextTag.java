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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Juergen Weber
 * Source file created on 18.02.2004
 *  
 */
public class TextTag extends BodyTagSupport
{

    public int doAfterBody()
    throws JspTagException
    {
        BodyContent body = getBodyContent();
        String s = body.getString();
        if (!s.matches("\\w*"))
        {
            throw new JspTagException("TextTag may not contain whitespace");
        }
        
        // TODO: get I18n from Property
        
        try
        {
            JspWriter out = body.getEnclosingWriter();
            out.print(s);
        }
        catch (IOException ioe)
        {
            System.out.println("Error in TextTag: " + ioe);
        }
        return (SKIP_BODY);
    }

}
