package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.model.NewMovieInputRequest;
import com.movieztalk.wikipedia.MoviesMetaData;
import com.movieztalk.wikipedia.WikipediaExtractor;

public class NewMovieServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		NewMovieInputRequest movieInputRequest = gson.fromJson(test, NewMovieInputRequest.class);
		PrintWriter out = response.getWriter();
		boolean result = insertNewMovieIntoDB(movieInputRequest);
		out.write(Boolean.toString(result));
		out.flush();
		out.close();
	}

	public boolean insertNewMovieIntoDB(NewMovieInputRequest movieInputRequest) throws IOException {
		WikipediaExtractor wikiExtractor = new WikipediaExtractor();
		String plot = wikiExtractor.fetchPlotString(movieInputRequest.getWikiUrl());
		MoviesMetaData metaData = wikiExtractor.fetchMovieMetaData(movieInputRequest.getWikiUrl());

		boolean result = false;
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");

			preparedStatement = connect.prepareStatement(
					"update movieztalk.movie set name=?, hashtag=?, wikiurl=?, songsandtrailers=?, videoreviews=?,interviewsandevents=?, plot=?,"
							+ " director=?, actors=?,release_date=?, boxoffice=?, budget=?,writers=?,currently_processed=? where movieid=?");
			preparedStatement.setString(1,
					Strings.isNullOrEmpty(movieInputRequest.getName()) ? "" : metaData.getName());
			preparedStatement.setString(2,
					Strings.isNullOrEmpty(movieInputRequest.getHashTag()) ? "" : movieInputRequest.getHashTag());
			preparedStatement.setString(3,
					Strings.isNullOrEmpty(movieInputRequest.getWikiUrl()) ? "" : movieInputRequest.getWikiUrl());
			preparedStatement.setString(4, Strings.isNullOrEmpty(movieInputRequest.getSongsAndTrailers()) ? ""
					: movieInputRequest.getSongsAndTrailers());
			preparedStatement.setString(5, Strings.isNullOrEmpty(movieInputRequest.getVideoReviews()) ? ""
					: movieInputRequest.getVideoReviews());
			preparedStatement.setString(6, Strings.isNullOrEmpty(movieInputRequest.getInterviewsAndEvents()) ? ""
					: movieInputRequest.getInterviewsAndEvents());
			preparedStatement.setString(7, Strings.isNullOrEmpty(plot) ? "" : plot);
			preparedStatement.setString(8, gson.toJson(metaData.getDirectors()));
			preparedStatement.setString(9, gson.toJson(metaData.getActors()));
			preparedStatement.setString(10, metaData.getReleaseDate());
			preparedStatement.setString(11, metaData.getBoxOfficeCollection());
			preparedStatement.setString(12, metaData.getBudget());
			preparedStatement.setString(13,
					Strings.isNullOrEmpty(movieInputRequest.getWriters()) ? "" : movieInputRequest.getWriters());
			preparedStatement.setString(14, Strings.isNullOrEmpty(movieInputRequest.getCurrentlyComputed()) ? ""
					: movieInputRequest.getCurrentlyComputed());
			preparedStatement.setString(15,
					Strings.isNullOrEmpty(movieInputRequest.getId()) ? "" : movieInputRequest.getId());

			preparedStatement.executeUpdate();
			result = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}

		pushHashWordInMovieClassificationTable(movieInputRequest.getId(), movieInputRequest.getHashTag(),
				movieInputRequest.getName());

		return result;
	}

	public void pushHashWordInMovieClassificationTable(String movieId, String hashWord, String movieName) {
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");

			preparedStatement = connect.prepareStatement(
					"insert into movieclassification (movieid, moviename, moviekeywords) values(?,?,?)");
			preparedStatement.setString(1, movieId);
			preparedStatement.setString(2, hashWord);
			preparedStatement.setString(3, movieName);
			preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
	}
}
