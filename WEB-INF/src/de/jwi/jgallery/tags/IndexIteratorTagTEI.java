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

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * @author Jürgen Weber
 * Source file created on 10.03.2004
 *  
 * @see http://java.sun.com/products/jsp/tutorial/TagLibraries13.html
 * 
 * This class is to have the IndexIteratorTag.CURRENTINDEX variable exist only
 * within the IndexIteratorTag scope.
 */
public class IndexIteratorTagTEI extends TagExtraInfo
{

    public VariableInfo[] getVariableInfo(TagData data)
    {
        VariableInfo info = new VariableInfo(IndexIteratorTag.CURRENTINDEX,
                IndexIteratorCursor.class.getClass().getName(), true, VariableInfo.NESTED);
        VariableInfo[] infos = { info };
        return infos;
    }
}