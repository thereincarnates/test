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

import com.google.gson.Gson;
import com.movieztalk.model.NewMovieInputRequest;
import com.movieztalk.reviewsextraction.WikiPediaExtraction;
import com.movieztalk.task.TaskState;
import com.movieztalk.wikipedia.WikipediaExtractor;
import com.movieztalk.db.DatabaseHelper;

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
		String plot = new WikipediaExtractor().fetchPlotString(movieInputRequest.getWikiUrl());

		boolean result = false;
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");

			preparedStatement = connect.prepareStatement(
					"update movieztalk.movie set name=?, hashtag=?, wikiurl=?, songsandtrailers=?, videoreviews=?,interviewsandevents=?, plot=?,"
					+ " director=?, actors=?,release_date=?, box_office=?, budget=?,writers=? where movieid=?");
			preparedStatement.setString(1, movieInputRequest.getName());
			preparedStatement.setString(2, movieInputRequest.getHashTag());
			preparedStatement.setString(3, movieInputRequest.getWikiUrl());
			preparedStatement.setString(4, movieInputRequest.getSongsAndTrailers());
			preparedStatement.setString(5, movieInputRequest.getVideoReviews());
			preparedStatement.setString(6, movieInputRequest.getInterviewsAndEvents());
			preparedStatement.setString(7, plot);
			preparedStatement.setString(8, movieInputRequest.getDirector());
			preparedStatement.setString(9, movieInputRequest.getActors());
			preparedStatement.setString(10, movieInputRequest.getReleaseDate());
			preparedStatement.setString(11, movieInputRequest.getBoxOffice());
			preparedStatement.setString(12, movieInputRequest.getBudget());
			preparedStatement.setString(13, movieInputRequest.getWriters());
			preparedStatement.setString(14, movieInputRequest.getId());
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
