package com.movieztalk.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.movieztalk.extraction.model.Tweet;

public class DatabaseHelper {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();
	private static DatabaseHelper instance;

	private DatabaseHelper() {
	}

	public static synchronized DatabaseHelper getInstance() {
		if (instance == null) {
			instance = new DatabaseHelper();
		}
		return instance;
	}

	public void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception ex) {

			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (Exception ex) {

			}
		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception ex) {

			}
		}
	}

	public void storeTweetsInDB(final List<Tweet> tweets) {
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				Connection connect = null;
				ResultSet resultSet = null;
				PreparedStatement preparedStatement = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");
					connect = DriverManager
							.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
					preparedStatement = connect
							.prepareStatement("insert into  movieztalk.Tweet_Table (tweetid, tweetstr, taskid) values(?,?,?)");
					for (Tweet tweet : tweets) {
						preparedStatement.setString(1, tweet.getTweetId());
						preparedStatement.setString(2, tweet.getTweetStr());
						preparedStatement.setString(3, tweet.getTaskid());
						preparedStatement.addBatch();
					}
					preparedStatement.executeBatch();
					System.out.println("executing batch");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					closeResources(connect, preparedStatement, resultSet);
				}
			}
		});
	}

}
