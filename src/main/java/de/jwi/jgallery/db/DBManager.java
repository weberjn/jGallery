
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

import javax.sql.DataSource;

/**
 * @author Jürgen Weber Source file created on 21.03.2004
 *  
 */
public class DBManager implements Serializable
{

	private DataSource dataSource;

	public DBManager(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public int getAndIncFolderCounter(String folder, boolean doInc)
			throws SQLException
	{
		String query;
		Connection conn = dataSource.getConnection();
		int counter = 0;

		if (conn != null)
		{
			try
			{
				Statement stmt = conn.createStatement();

				query = "SELECT id FROM folders where folder='$FOLDER'";
				query = query.replaceAll("\\$FOLDER", folder);
				ResultSet rs = stmt.executeQuery(query);

				if (!rs.next())
				{
					// even with doInc = false create the folder in the database
					
					query = "INSERT INTO folders (folder) VALUES ('$FOLDER')";
					query = query.replaceAll("\\$FOLDER", folder);
					stmt.executeUpdate(query);
				}

				if (doInc)
				{
					query = "UPDATE folders SET hits = hits + 1 where folder='$FOLDER'";
					query = query.replaceAll("\\$FOLDER", folder);
					stmt.executeUpdate(query);
				}

				query = "SELECT hits FROM folders where folder='$FOLDER'";
				query = query.replaceAll("\\$FOLDER", folder);
				rs = stmt.executeQuery(query);

				if (rs.next())
				{
					counter = rs.getInt(1);
				}
			}
			finally
			{
				conn.close();
			}
		}

		return counter;
	}

	public int getAndIncImageCounter(String folder, String image, boolean doInc)
			throws SQLException
	{
		String query;
		Connection conn = dataSource.getConnection();
		int counter = 0;
		int id = 0;

		if (conn != null)
		{
			try
			{
				Statement stmt = conn.createStatement();

				query = "SELECT id FROM folders f where folder='$FOLDER'";
				query = query.replaceAll("\\$FOLDER", folder);
				ResultSet rs = stmt.executeQuery(query);

				if (rs.next())
				{
					id = rs.getInt(1);

				}

				String fid = Integer.toString(id);

				query = "SELECT id FROM images WHERE folderid='$FID' and image='$IMAGE'";
				query = query.replaceAll("\\$FID", fid);
				query = query.replaceAll("\\$IMAGE", image);

				rs = stmt.executeQuery(query);

				// is image allready in table ?

				if (!rs.next())
				{
					// even with doInc = false create the folder in the database
					
					query = "INSERT INTO images (folderid,image) VALUES ('$FID','$IMAGE')";
					query = query.replaceAll("\\$FID", fid);
					query = query.replaceAll("\\$IMAGE", image);
					stmt.executeUpdate(query);
				}

				if (doInc)
				{
					query = "UPDATE images SET hits = hits + 1 where folderid='$FID' and image='$IMAGE'";
					query = query.replaceAll("\\$FID", fid);
					query = query.replaceAll("\\$IMAGE", image);
					stmt.executeUpdate(query);
				}
				
				query = "SELECT hits FROM images where folderid='$FID' and image='$IMAGE'";
				query = query.replaceAll("\\$FID", fid);
				query = query.replaceAll("\\$IMAGE", image);
				rs = stmt.executeQuery(query);

				if (rs.next())
				{
					counter = rs.getInt(1);
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