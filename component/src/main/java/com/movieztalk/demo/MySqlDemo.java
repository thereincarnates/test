package com.movieztalk.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
		preparedStatement.setString(1, "Test");
		preparedStatement.executeUpdate();
		connect.close();
		preparedStatement.close();
		
		
	}
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
		new MySqlDemo().writeExample();
	}

	public boolean pushTweets(List<Tweet> tweets) {

		boolean result = false;
		return result;
	}

}
