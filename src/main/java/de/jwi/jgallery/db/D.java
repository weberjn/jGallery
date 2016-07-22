
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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author Jürgen Weber Source file created on 21.03.2004
 *  
 */
public class D implements Serializable
{

	private DataSource dataSource;

	public D(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}


	public int updateCounters()
			throws SQLException
	{
		String query;
		Connection conn = dataSource.getConnection();
		int counter = 0;
		int id = 0;
		List l = new ArrayList();

		if (conn != null)
		{
			try
			{
				Statement stmt = conn.createStatement();

				query = "SELECT id FROM folders";
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next())
				{
					id = rs.getInt(1);
					l.add(new Integer(id));

				}

				for (int i=0;i<l.size();i++)
				{
					id = ((Integer)l.get(i)).intValue();
					
					String fid = Integer.toString(id);

					query = "SELECT max(hits) FROM images WHERE folderid='$FID'";
					query = query.replaceAll("\\$FID", fid);

					rs = stmt.executeQuery(query);
					
					if (rs.next())
					{
						counter = rs.getInt(1);
					}
					
					query = "UPDATE folders SET hits = '$HITS' where id='$FID'";
					
					query = query.replaceAll("\\$HITS", Integer.toString(counter));
					query = query.replaceAll("\\$FID", fid);
					
					stmt.executeUpdate(query);
				}
			}
			finally
			{
				conn.close();
			}
		}

		return counter;
	}
}