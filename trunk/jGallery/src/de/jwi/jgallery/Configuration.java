
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


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;


/**
 * @author Jürgen Weber Source file created on 29.02.2004
 *  
 */
public class Configuration implements Serializable
{

	private Properties properties;

	private Configuration parent = null;
	
	private IThumbnailWriter thumbnailWriter;

	public Configuration(Properties properties)
	{
		this.properties = properties;
	}

	
	public Configuration(Configuration parent)
	{
		this.parent = parent;
		properties = new Properties();
	}

	public void addProperty(String key, String val)
	{
		properties.put(key, val);
	}

	public Configuration(InputStream is, Configuration parent)
			throws IOException
	{
		this.parent = parent;

		properties = new Properties();

		if (is != null)
		{
			properties.load(is);
		}
	}

	public String toString()
	{
		return properties.toString();
	}

	public Configuration(InputStream is) throws IOException
	{
		this(is, null);
	}

	public int getInt(String key, int defaultValue)
	{
		String s = getString(key);
		if (s==null)
		{
			return defaultValue;
		}
		return Integer.parseInt(s);
	}


	public float getFloat(String key, float defaultValue)
	{
		String s = getString(key);
		if (s==null)
		{
			return defaultValue;
		}
		
		return Float.parseFloat(s);
	}


	public boolean getBoolean(String key, boolean defaultValue)
	{
		String s = getString(key);
		if (s==null)
		{
			return defaultValue;
		}
		
		return Boolean.valueOf(s).booleanValue();
	}

	public String getString(String key)
	{
		return getString(key,null);
	}
	
	public String getString(String key, String defaultValue)
	{
		String val = properties.getProperty(key);
		if (null == val)
		{
			if (null != parent)
			{
				val = parent.getString(key);
			}
		}
		if (val == null)
		{
			val = defaultValue;
		}
		return val;
	}

	/**
	 * Add all entries of this Configuration starting with variable (e.g.
	 * variable.copyright) to a map, but only if they do not exist, yet. This
	 * has the effect of variables of parent Configurations overwriting this
	 * Configuration's variables.
	 * 
	 * @param map
	 *            the map
	 */
	void getUserVariables(Map map) throws GalleryException
	{
		for (Enumeration e = properties.propertyNames(); e.hasMoreElements();)
		{
			String key = (String) e.nextElement();
			if (key.startsWith("variable."))
			{
				String name = key.substring(key.indexOf('.') + 1);
				if (name.length() < 1)
				{
					throw new GalleryException("invalid variable entry: " + key);
				}
				if (!map.containsKey(name))
				{
					map.put(name, properties.getProperty(key));
				}
			}

		}
		if (null != parent)
		{
			parent.getUserVariables(map);
		}
	}

	public IThumbnailWriter getThumbnailWriter()
	{
		IThumbnailWriter writer = this.thumbnailWriter; 
		
		if (null == writer)
		{
			if (null != parent)
			{
				writer = parent.getThumbnailWriter();
			}
		}
		return writer;
	}
	
	public void setThumbnailWriter(IThumbnailWriter thumbnailWriter)
	{
		this.thumbnailWriter = thumbnailWriter;
	}
}