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
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Movie;
import com.movieztalk.movieaspects.model.MovieAspect;
import com.movieztalk.task.TaskState;

public class HomeCarouselServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(HomeCarouselServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("applicaton/json");
		logger.info("Testing the data");

		// List<Movie> movies = getListOfMovies();
		List<Movie> movies = fetchCarouselMovies();
		Gson gson = new Gson();
		String json = gson.toJson(movies);

		System.out.println("Movie json Object :  " + json);

		PrintWriter out = response.getWriter();
		out.write(json);

		out.flush();
		out.close();
	}

	private List<Movie> fetchCarouselMovies() {
		List<Movie> movies = new ArrayList<>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Instant before = Instant.now();
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			Instant after = Instant.now();
			logger.warning("done creating connection in:" + Duration.between(before, after).toMillis() + " ms.");
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from movie where movieid in (select movieid from carousel_movies)");
			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setName(resultSet.getString("name"));
				movie.setBudget(resultSet.getString("budget"));
				movie.setBoxOffice(resultSet.getString("boxoffice"));
				movie.setOverall(new MovieAspect().setRating(resultSet.getString("overallrating") == null ? 1
						: Double.parseDouble(resultSet.getString("overallrating"))));
				movie.setStory(new MovieAspect().setRating(resultSet.getString("storyrating") == null ? 1
						: Double.parseDouble(resultSet.getString("storyrating"))));
				movie.setActing(new MovieAspect().setRating(resultSet.getString("actingrating") == null ? 1
						: Double.parseDouble(resultSet.getString("actingrating"))));
				movie.setDirection(new MovieAspect().setRating(resultSet.getString("directionrating") == null ? 1
						: Double.parseDouble(resultSet.getString("overallrating"))));
				movie.setMusic(new MovieAspect().setRating(resultSet.getString("musicrating") == null ? 1
						: Double.parseDouble(resultSet.getString("musicrating"))));
				movie.setImageUrl(resultSet.getString("imageurl"));
				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return movies;
	}
}
