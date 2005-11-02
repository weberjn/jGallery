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


import java.util.Comparator;


/**
 * @author J�rgen Weber
 * Source file created on 08.03.2004
 */
public class ImageComparator implements Comparator
{
    public static final int SORTNONE = 0;
    public static final int SORTBYNAME = 1;
    public static final int SORTBYFILEDATE = 2;
    public static final int SORTBYEXIFDATE = 3;
    public static final int SORT_ASC = 0;
    public static final int SORT_DESC = 1;
    
    private int sortBy,sortOrder;
    
    ImageComparator(int sortBy,int sortOrder) throws GalleryException
    {
        this.sortBy = sortBy;
        
        if ((sortBy != SORTBYFILEDATE) && (sortBy != SORTBYNAME) && (sortBy != SORTBYEXIFDATE))
        {
            throw new GalleryException("illegal sorting mode");
        }
        
        this.sortOrder = sortOrder;
        
        if ((sortOrder != SORT_ASC) && (sortOrder != SORT_DESC))
        {
            throw new GalleryException("illegal sorting mode");
        }
    }
    
    public int compare(Object o1,
            Object o2)
    {
        int rc = 0;
        
        Image i1 = (Image)o1;
        Image i2 = (Image)o2;
        if (SORTBYFILEDATE == sortBy)
        {
            rc = (int) (i1.getLastModified() - i2.getLastModified()) ;
        }
        else if (SORTBYEXIFDATE == sortBy)
        {
            EXIFInfo e1 = i1.getExif();
            EXIFInfo e2 = i2.getExif();
            if ((null==e1) || (null==e2))
            {
                rc = 0;
            }
            else 
            {
                String s1 = e1.getOriginalDate();
                String s2 = e2.getOriginalDate();
                
                if ((null==s1) || (null==s2))
                {
                    rc = 0;
                }
                else 
                {
                    rc = s1.compareTo(s2);
                }
            }
        }
        else if (SORTBYNAME == sortBy)
        {
            rc= i1.getName().compareToIgnoreCase(i2.getName());
        }
        
        if (sortOrder==SORT_DESC)
        {
        	rc*=-1;
        }
        
        return rc;
    }
}
