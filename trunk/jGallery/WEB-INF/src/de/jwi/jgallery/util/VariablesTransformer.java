package de.jwi.jgallery.util;

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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VariablesTransformer tries to automatically replace variable names in JAlbum skins
 * to JGallery variable names.
 * @author Jürgen Weber
 * Source file created on 01.03.2004
 *  
 */
public class VariablesTransformer
{
    private static String loadFile(InputStream is) throws IOException
    {
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        String s = new String(bytes);
        return s;
    }

    public static void main(String[] args) throws IOException
    {
        if (args.length<1)
        {
            System.out.println(VariablesTransformer.class.getClass().getName() 
                    + " <file to transform>");
            System.exit(1);
        }
        
        InputStream is = VariablesTransformer.class
                .getResourceAsStream("variables.properties");

        String vardefs = loadFile(is);

        is = new FileInputStream(args[0]);

        String s = loadFile(is);
        String old,nuw;

        // replace $text.aWord with <jg:text>aWord</jg:text>
        String regex = "\\$text\\.\\w*";
        
        Pattern pattern = Pattern.compile(regex);
        
        Matcher matcher = pattern.matcher(s);

        String s0 = new String(s);
        
        while(matcher.find()) 
        {
            old = "\\" + matcher.group();
            int p = old.indexOf('.');
            nuw = "<jg:text>" + old.substring(p+1)+ "</jg:text>";
            s0 = s0.replaceAll(old,nuw);
        }        
        
        s = s0;

        // replace other variables
        
        String[] s1 = vardefs.trim().split("[{}]");

        for (int i=0;i<s1.length;i+=2)
        {
            String clazz = s1[i].trim();
            String vars = s1[i+1].trim();
            
            String[] v = vars.split("\\s+"); // whitespace
            
            for (int j=0;j<v.length;j++)
            {
                old = "\\$"+v[j];
                nuw = "\\${"+clazz+"."+v[j]+"}";
                //System.out.println("replacing "+old+" with "+ nuw);
                s = s.replaceAll(old,nuw);
                
                old="exists=\""+v[j]+"\"";
                nuw = "exists=\"\\${"+clazz+"."+v[j]+"}\"";
                s = s.replaceAll(old,nuw);

                old="exists="+v[j];
                nuw = "exists=\"\\${"+clazz+"."+v[j]+"}\"";
                s = s.replaceAll(old,nuw);

            }
        }
        
        System.out.println("\n\n"+s);
        
        FileOutputStream fos = new FileOutputStream(args[0]+".jsp");
        
        s0 = "<%@ taglib uri=\"http://www.jwi.de/jGallery/taglib\" prefix=\"jg\" %>\n\n";
        
        fos.write(s0.getBytes());

        old="href=\"http://www.datadosen.se/jalbum\"";
        nuw="href=\"\\${folder.generatorurl}\"";
        s = s.replaceAll(old,nuw);
        
        old="<ja:include";
        nuw="<jsp:include";
        s = s.replaceAll(old,nuw);

        old="</ja:include";
        nuw="</jsp:include";
        s = s.replaceAll(old,nuw);
        
        old="<ja:";
        nuw="<jg:";
        s = s.replaceAll(old,nuw);
        
        old="</ja:";
        nuw="</jg:";
        s = s.replaceAll(old,nuw);
        
        
        fos.write(s.getBytes());
        fos.close();
          
        if (s.indexOf("<jg:eval>")>0)
        {
            System.out.println("Please update <jg:eval> Tags with JSP EL.");
        }
        
    }
}
