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


import java.util.Stack;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Juergen Weber
 * Source file created on 18.02.2004
 *  
 */
public class IfTag extends BodyTagSupport
{

    private String exists = null;

    private String test = null;

    boolean expressionResult;

    public void setExists(String exists)
    {
        this.exists = exists;
    }

    public void setTest(String test)
    {
        this.test = test;
    }

    private void pushResult(boolean result)
    {
        ServletRequest request = pageContext.getRequest();
        Stack stack = (Stack) request.getAttribute("ifThenStack");
        if (null == stack)
        {
            stack = new Stack();
            request.setAttribute("ifThenStack", stack);
        }
        stack.push(new Boolean(result));
    }

    public int doStartTag() throws JspException
    {
        if (null != test)
        {
            if ("true".equals(test))
            {
                pushResult(true);
                return EVAL_BODY_INCLUDE;
            }
            else 
            {
                pushResult(false);
                return SKIP_BODY;
            }
        }
        else if (null != exists)
        {
            if (exists.length()>0 )
            {
                pushResult(true);
                return EVAL_BODY_INCLUDE;
            }
            else
            {
                pushResult(false);
                return SKIP_BODY;
            }
            
        }
        else
        {
            throw new JspException("illegal attribute for tag 'if'");
        }
    }
}
