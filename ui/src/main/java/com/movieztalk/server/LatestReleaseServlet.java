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
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Movie;

public class LatestReleaseServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(LatestReleaseServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Movie> movies = fetchLatestMovies();
		Gson gson = new Gson();
		String json = gson.toJson(movies);

		PrintWriter out = response.getWriter();
		out.write(json);

		out.flush();
		out.close();
	}

	private List<Movie> fetchLatestMovies() {
		List<Movie> movies = new ArrayList<>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Instant before = Instant.now();
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			Instant after = Instant.now();
			logger.warning("done creating connection in:" + Duration.between(before, after).toMillis() +" ms.");
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from movie where movieid in (select movieid from latest_releases)");
			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setName(resultSet.getString("name"));
				movie.setBudget(resultSet.getString("budget"));
				movie.setBoxOffice(resultSet.getString("boxoffice"));
				movies.add(movie);
			}
		} catch (SQLException e) {
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return movies;
	}
}
