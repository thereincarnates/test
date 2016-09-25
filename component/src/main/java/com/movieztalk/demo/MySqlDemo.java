package com.movieztalk.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Tweet;

public class MySqlDemo {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public void readExample() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from Tweet_Table");
		while (resultSet.next()) {
			String tweetId = resultSet.getString("tweetid");
			System.out.println(tweetId);
		}
		connect.close();
	}
	
	public void writeExample() throws ClassNotFoundException, SQLException{
		connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
		preparedStatement = connect
		          .prepareStatement("insert into  movieztalk.Tweet_Table (tweetid) values(?)");
		preparedStatement.setString(1, "Test1");
		preparedStatement.addBatch();
		preparedStatement.setString(1, "Test2");
		preparedStatement.addBatch();
		preparedStatement.setString(1, "Test3");
		preparedStatement.addBatch();
		preparedStatement.executeBatch();
		connect.close();
		preparedStatement.close();
		
		
	}
	
	private void storeUpdatedTweets(List<Tweet> tweets) throws SQLException {
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try{
		connect = DriverManager
				.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
		preparedStatement = connect
				.prepareStatement("update Tweet_Table set movieid =?, compname=?, sentiment=? where rowid=?");
		for (Tweet tweet : tweets) {
			preparedStatement.setString(1, tweet.getMovieId());
			preparedStatement.setString(2, "testCompName" /*tweet.getCompName()*/);
			preparedStatement.setString(3, tweet.getSentiment());
			preparedStatement.setInt(4, tweet.getRowId());
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		}finally{
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
	}
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
		Tweet tweet = new Tweet();
		tweet.setRowId(1731).setCompName("xxx");
		new MySqlDemo().storeUpdatedTweets(Arrays.asList(tweet));
	}

	public boolean pushTweets(List<Tweet> tweets) {

		boolean result = false;
		return result;
	}

}
