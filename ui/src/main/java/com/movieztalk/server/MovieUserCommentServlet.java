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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;

@WebServlet("/movieUserComment")
public class MovieUserCommentServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		String userCommentData = CharStreams.toString(request.getReader());
		System.out.println("testing UserCOmment" + userCommentData);
		response.getWriter().write(userCommentData);
		UserComment userComment = new Gson().fromJson(userCommentData,
				UserComment.class);

		Connection connect = null;
		int resultSet;
		Statement statement = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ServerConfiguration mysqlserver = new ServerConfiguration();
		try {

			connect = DriverManager.getConnection("jdbc:mysql://"
					+ mysqlserver.mysqlServerName + ":"
					+ mysqlserver.mysqlServerPort + "/"
					+ mysqlserver.mysqlDBName + "?user="
					+ mysqlserver.mysqlServerUserName + "&password="
					+ mysqlserver.mysqlServerPassword);

			statement = connect.createStatement();
			resultSet = statement
					.executeUpdate("insert into usercomment (userid,movieid,comment,overallRating,"
							+ "storyRating,directionRating,musicRating) VALUES ('"
							+ userComment.getUserId()
							+ "',"
							+ userComment.getMovieId()
							+ ",'"
							+ userComment.getComment()
							+ "',"
							+ userComment.getOverallRating()
							+ ","
							+ userComment.getStoryRating()
							+ ","
							+ userComment.getDirectionRating()
							+ ","
							+ userComment.getMusicRating() + ")");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement,
					null);
		}

	}

}

class UserComment {

	String movieId;
	String userId;
	String comment;
	int overallRating;
	int storyRating;
	int actingRating;
	int directionRating;
	int musicRating;

	public String getMovieId() {
		return movieId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(int overallRating) {
		this.overallRating = overallRating;
	}

	public int getStoryRating() {
		return storyRating;
	}

	public void setStoryRating(int storyRating) {
		this.storyRating = storyRating;
	}

	public int getActingRating() {
		return actingRating;
	}

	public void setActingRating(int actingRating) {
		this.actingRating = actingRating;
	}

	public int getDirectionRating() {
		return directionRating;
	}

	public void setDirectionRating(int directionRating) {
		this.directionRating = directionRating;
	}

	public int getMusicRating() {
		return musicRating;
	}

	public void setMusicRating(int musicRating) {
		this.musicRating = musicRating;
	}

}
