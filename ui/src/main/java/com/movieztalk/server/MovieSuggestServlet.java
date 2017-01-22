package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@WebServlet("/movieSuggestServlet")
public class MovieSuggestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Gson gson = new Gson();
		PrintWriter out = response.getWriter();

		response.setContentType("application/json");
		String movieNameInitials = request.getParameter("mvNameInitials");
		List<List<String>> movieNameList = new ArrayList<List<String>>();
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("moviesAutoComplete");
		Element element = cache.get(movieNameInitials);
		if (element != null) {
			movieNameList = (List<List<String>>) element.getObjectValue();
			String json = gson.toJson(movieNameList);
			System.out.println("Start" + new Date());
			out.write(json);
			out.flush();
			out.close();
			return;
		}

		Connection connect = null;
		ResultSet resultSet = null;
		Statement statement = null;
		ServerConfiguration mysqlserver = new ServerConfiguration();
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + mysqlserver.mysqlServerName + ":"
					+ mysqlserver.mysqlServerPort + "/" + mysqlserver.mysqlDBName + "?user="
					+ mysqlserver.mysqlServerUserName + "&password=" + mysqlserver.mysqlServerPassword);

			statement = connect.createStatement();
			resultSet = statement.executeQuery(
					"select name,movieid,release_date from movie where name like '" + movieNameInitials + "%' limit 10");
			while (resultSet.next()) {
				List<String> movieName = new ArrayList<String>();
				movieName.add(resultSet.getString("movieid"));
				movieName.add(resultSet.getString("name") + "(" + resultSet.getString("release_date") + ")");
				movieNameList.add(movieName);
			}
			cache.put(new Element(movieNameInitials, movieNameList));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}

		String json = gson.toJson(movieNameList);
		System.out.println("Start" + new Date());
		out.write(json);
		out.flush();
		out.close();
	}

}
