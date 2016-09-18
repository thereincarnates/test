package com.movieztalk.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDemo {

	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;

	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?"
              + "user=root&password=root");
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from Tweet_Table");
		while(resultSet.next()){
			String  tweetId = resultSet.getString("tweetid");
			System.out.println(tweetId);
		}
		connect.close();
	}
}
