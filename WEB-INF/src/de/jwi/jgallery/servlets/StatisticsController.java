
package de.jwi.jgallery.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import de.jwi.jgallery.db.Statistics;

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

/**
 * @author Jürgen Weber Source file created on 08.08.2004
 *  
 */
public class StatisticsController extends HttpServlet
{

	private Statistics statistics;

	private static String version = "unknown";

	public void init() throws ServletException
	{
		String dataSource = (String) getServletContext().getInitParameter(
				"dataSource");

		Context context;
		try
		{
			context = new InitialContext();
			if (context == null)
			{
				throw new ServletException("Boom - No Context");
			}
			DataSource ds = (DataSource) context.lookup(dataSource);

			statistics = new Statistics(ds);
		}
		catch (NamingException e)
		{
			throw new ServletException(e.getMessage(), e);
		}

		try
		{
			InputStream is = getServletContext().getResourceAsStream(
					"/WEB-INF/" + Controller.VERSIONCONFIGFILE);

			Properties versionProperties = new Properties();
			versionProperties.load(is);

			String s = versionProperties.getProperty("version");
			if (null != s)
			{
				version = s;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.err); // TODO
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{

		String self = null;
		String contextPath = null;

		contextPath = request.getContextPath();
		String servletPath = request.getServletPath();

		String pathInfo = request.getPathInfo();

		self = contextPath + servletPath;

		
		if (pathInfo != null
				&& (pathInfo.startsWith("/to/") || pathInfo.startsWith("/to/")))
		{
			doGetDetail(request,response, self);
			return;
		}


		try
		{
			List l = statistics.getStatistics(request, self);
			request.setAttribute("statistics", l);
		}
		catch (SQLException e)
		{
			throw new ServletException(e.getMessage(), e);
		}


		request.setAttribute("self", self);
		
		request.setAttribute("context", contextPath);

		Date d = new Date();
		request.setAttribute("date", DateFormat
				.getDateInstance(DateFormat.FULL).format(d));
		request.setAttribute("time", DateFormat.getTimeInstance().format(d));

		request.setAttribute("version", version);

		request.setAttribute("serverInfo", getServletContext().getServerInfo());

		String forward = "/WEB-INF/statistics.jsp";

		RequestDispatcher requestDispatcher = getServletContext()
				.getRequestDispatcher(forward);

		requestDispatcher.forward(request, response);

	}

	public void doGetDetail(HttpServletRequest request,
			HttpServletResponse response,String self) throws IOException, ServletException
	{
		boolean withThumbs = false;
		String pathInfo = request.getPathInfo();
		withThumbs = pathInfo.startsWith("/tn/");
		
		String id = pathInfo.substring(4);
		
		try
		{
			Map m = statistics.getFolderStatistics(id, request, self);
			request.setAttribute("statistics", m.get("images"));
			request.setAttribute("folder", m.get("folder"));
		}
		catch (SQLException e)
		{
			throw new ServletException(e.getMessage(), e);
		}

		Date d = new Date();
		request.setAttribute("date", DateFormat
				.getDateInstance(DateFormat.FULL).format(d));
		request.setAttribute("time", DateFormat.getTimeInstance().format(d));

		request.setAttribute("self", self);
		
		request.setAttribute("context", request.getContextPath());
		
		String forward = "/WEB-INF/statisticsdet.jsp";

		RequestDispatcher requestDispatcher = getServletContext()
				.getRequestDispatcher(forward);

		requestDispatcher.forward(request, response);
		
	}
}