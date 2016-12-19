package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.extraction.model.Movie;

@WebServlet("/movieSuggestServlet")
public class MovieSuggestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		String movieNameInitials = request.getParameter("mvNameInitials");

		List<List<String>> movieNameList = new ArrayList<List<String>>();
		Connection connect = null;
		ResultSet resultSet = null;
		Statement statement = null;
		ServerConfiguration mysqlserver = new ServerConfiguration();
		try {
			connect = DriverManager.getConnection("jdbc:mysql://"
					+ mysqlserver.mysqlServerName + ":"
					+ mysqlserver.mysqlServerPort + "/"
					+ mysqlserver.mysqlDBName + "?user="
					+ mysqlserver.mysqlServerUserName + "&password="
					+ mysqlserver.mysqlServerPassword);

			Instant after = Instant.now();

			statement = connect.createStatement();
			resultSet = statement.executeQuery("select name,movieid from movie where name like '" +movieNameInitials+"%'");
			while (resultSet.next()) {
				List<String> movieName = new ArrayList<String>();
				movieName.add(resultSet.getString("name"));
				movieName.add(resultSet.getString("movieid"));
				movieNameList.add(movieName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		Gson gson = new Gson();
		String json = gson.toJson(movieNameList);
		PrintWriter out = response.getWriter();
		System.out.println("Start" + new Date());
		out.write(json);
		out.flush();
		out.close();

	}

}
