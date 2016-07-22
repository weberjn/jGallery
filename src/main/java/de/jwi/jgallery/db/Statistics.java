
package de.jwi.jgallery.db;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * @author Jürgen Weber Source file created on 08.08.2004
 *  
 */
public class Statistics
{
	private static NumberFormat numberInstance = NumberFormat.getNumberInstance();
	static
	{
		numberInstance.setMaximumFractionDigits(1);	
	}
	
	public class FolderInfo
	{
		
		
		public String toString()
		{

			return name + " " + hits + " " + max + " " + avg;
		}

		private String name;
		
		private String id;

		private String hits;

		private String max;

		private String min;

		private String avg;
		
		private String baseUrl;

		private String servletURL;
		
		public FolderInfo(String name, String id, String hits, String max, String min,
				String avg, String baseUrl, String servletURL)
		{
			this.name = name;
			this.id = id;
			this.hits = hits;
			this.max = (max != null) ? max : "0";
			this.min = (min != null) ? min : "0";
			this.avg = (avg != null) ? avg : "0";
			
			
			
			float f = Float.parseFloat(this.avg);
			this.avg = numberInstance.format(f);
			
			this.baseUrl = baseUrl;
			this.servletURL = servletURL;
		}


		public String getURL()
		{
			return baseUrl + name + "index.html?nocount=true";
		}
		
		public String getDetailsTO()
		{
			return servletURL + "/to/"+id;
		}

		public String getDetailsTN()
		{
			return servletURL + "/tn/"+id;
		}

		
		public String getAvg()
		{
			return avg;
		}

		public String getName()
		{
			return name;
		}

		public String getId()
		{
			return id;
		}

		public String getHits()
		{
			return hits;
		}


		public String getMax()
		{
			return max;
		}

		public String getMin()
		{
			return min;
		}

	}
	
	public class ImageInfo
	{
		private String name;
		private String hits;
		private String url;
		public ImageInfo(String name, String hits, String url)
		{
			this.name = name;
			this.hits = hits;
			this.url = url;
		}
		public String getHits()
		{
			return hits;
		}
		public String getName()
		{
			return name;
		}
		public String getUrl()
		{
			return url;
		}
	}

	private DataSource dataSource;

	public Statistics(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public List getStatistics(HttpServletRequest request, String self) throws SQLException
	{
		String query;
		Connection conn = dataSource.getConnection();
		int counter = 0;

		List l = new ArrayList();
		FolderInfo fi;

		if (conn != null)
		{
			Statement stmt = conn.createStatement();

			query = "select folder, f.id, f.hits as folderhits, " +
					"max(i.hits) as maxhits, min(i.hits) as minhits, " +
					"avg(i.hits) as avghits " +
					"from folders f left join images i on (f.id = i.folderid) " +
					"group by f.id order by f.hits desc;";
			
			ResultSet rs = stmt.executeQuery(query);

			String baseUrl = request.getContextPath();
				
			while (rs.next())
			{
				fi = new FolderInfo(rs.getString("folder"), rs.getString("id"),
						rs.getString("folderhits"), rs.getString("maxhits"), rs
						.getString("minhits"), rs.getString("avghits"),baseUrl,self);
				
				l.add(fi);
			}

			conn.close();
		}

		return l;
	}

	public Map getFolderStatistics(String id, HttpServletRequest request, String self) throws SQLException
	{
		String query;
		Connection conn = dataSource.getConnection();
		int counter = 0;

		List l = new ArrayList();
		Map h = new HashMap();
		ImageInfo ii;

		if (conn != null)
		{
			Statement stmt = conn.createStatement();

			query = "select folder " +
			"from folders " +
			"where id='" + id +
			"';";
	
			ResultSet rs = stmt.executeQuery(query);

			String folder = null;
			if (rs.next())
			{
				folder = rs.getString("folder");
			}
			
			h.put("folder",folder);
			
			query = "select image as name, hits " +
					"from images " +
					"where folderid='" + id +
					"' order by hits desc;";
			
			rs = stmt.executeQuery(query);

			String baseUrl = request.getContextPath();
				
			while (rs.next())
			{
				String name = rs.getString("name");
				int p=-1;
				if ((p=name.indexOf('.'))>-1)
				{
					name = name.substring(0,p);
				}
				ii = new ImageInfo(name, rs.getString("hits"),baseUrl+folder+name+".html?nocount=true");
				
				l.add(ii);
			}

			conn.close();
		}

		h.put("images",l);
		
		return h;
	}

}