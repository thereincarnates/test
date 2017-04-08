package com.movieztalk.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.server.ServerConfiguration;

public class MovieDetailsHelper {
	private static MovieDetailsHelper instance;
	private ServerConfiguration serverConfiguration;

	private MovieDetailsHelper() {
		serverConfiguration = ServerConfiguration.getInstance();
	}

	public static synchronized MovieDetailsHelper getInstance() {
		if (instance == null) {
			instance = new MovieDetailsHelper();
		}
		return instance;
	}

	public boolean isMovieVisibleOnPortal(@Nullable String movieId) {
		if (Strings.isNullOrEmpty(movieId)) {
			return false;
		}

		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String result = "";
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			statement = connect.createStatement();
			resultSet = statement.executeQuery(
					"select movieid from movie where movieid='" + movieId + "' and is_visible_on_portal='Y'");
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return false;
	}

	public String fetchLatestMovieId() {

		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String result = "";
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			statement = connect.createStatement();
			resultSet = statement.executeQuery(
					"select movieid from movie where is_visible_on_portal='Y' order by movieid desc limit 1");
			while (resultSet.next()) {
				result = resultSet.getString("movieid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return result;
	}

}
