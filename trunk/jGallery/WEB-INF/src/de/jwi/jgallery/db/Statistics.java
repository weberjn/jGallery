
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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * @author J�rgen Weber Source file created on 08.08.2004
 *  
 */
public class Statistics
{

	public class FolderInfo
	{
		public String toString()
		{

			return name + " " + hits + " " + max + " " + avg;
		}

		private String name;

		private String hits;

		private String max;

		private String min;

		private String avg;
		
		private String baseUrl;

		public FolderInfo(String name, String hits, String max, String min,
				String avg, String baseUrl)
		{
			this.name = name;
			this.hits = hits;
			this.max = max;
			this.min = min;
			this.avg = avg;
			this.baseUrl = baseUrl;
		}


		public String getURL()
		{
			return baseUrl + name + "index.html?nocount=true";
		}
		
		
		public String getAvg()
		{
			return avg;
		}

		public String getName()
		{
			return name;
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

	private DataSource dataSource;

	public Statistics(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public List getStatistics(HttpServletRequest request) throws SQLException
	{
		String query;
		Connection conn = dataSource.getConnection();
		int counter = 0;

		List l = new ArrayList();
		FolderInfo fi;

		if (conn != null)
		{
			Statement stmt = conn.createStatement();

			query = "select folder, f.hits as folderhits, "
					+ "max(i.hits) as maxhits, min(i.hits) as minhits, avg(i.hits) as avghits "
					+ "from images i, folders f where f.id = i.folderid group by f.id "
					+ "order by f.hits desc";
			ResultSet rs = stmt.executeQuery(query);

			String baseUrl = request.getContextPath();
				
			while (rs.next())
			{
				fi = new FolderInfo(rs.getString("folder"), rs
						.getString("folderhits"), rs.getString("maxhits"), rs
						.getString("minhits"), rs.getString("avghits"),baseUrl);
				
				l.add(fi);
			}

			conn.close();
		}

		return l;
	}


}