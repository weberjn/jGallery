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


import java.util.EmptyStackException;
import java.util.Stack;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Juergen Weber
 * Source file created on 18.02.2004
 *  
 */
public class ElseTag extends BodyTagSupport
{

    private boolean popResult()
    throws JspException
    {
        Boolean b;

        ServletRequest request = pageContext.getRequest();
        Stack stack = (Stack) request.getAttribute("ifThenStack");
        if (null != stack)
        {
            try
            {
                b = (Boolean) stack.pop();
            }
            catch (EmptyStackException e)
            {
                throw new JspException("EmptyStackException: Else Tag without If");
            }
        }
        else
        {
            throw new JspException("Else Tag without If");
        }
        return b.booleanValue();
    }

    public int doStartTag() throws JspException
    {
        boolean lastResult = popResult();
        if (lastResult)
        {
            return SKIP_BODY;
        }
        else 
        {
            return EVAL_BODY_INCLUDE;
        }
    }
}
