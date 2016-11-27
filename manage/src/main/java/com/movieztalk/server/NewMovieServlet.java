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
import com.movieztalk.task.TaskState;
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
	
	public boolean insertNewMovieIntoDB(NewMovieInputRequest movieInputRequest){
		boolean result = false;
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			preparedStatement = connect
					.prepareStatement("insert into  movieztalk.movie (name, hashtag, wikiurl, songsandtrailers, videoreviews, interviewsandevents) values(?,?,?,?,?,?)");
			preparedStatement.setString(1, movieInputRequest.getName());
			preparedStatement.setString(2, movieInputRequest.getHashTag());
			preparedStatement.setString(3, movieInputRequest.getWikiUrl());
			preparedStatement.setString(4, movieInputRequest.getSongsAndTrailers());
			preparedStatement.setString(5, movieInputRequest.getVideoReviews());
			preparedStatement.setString(6, movieInputRequest.getInterviewsAndEvents());
			result = preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
		return result;
	}
}
