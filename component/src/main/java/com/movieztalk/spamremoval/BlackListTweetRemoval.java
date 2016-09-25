package com.movieztalk.spamremoval;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.movieztalk.TweetProcessor;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Tweet;

public class BlackListTweetRemoval implements TweetProcessor{
	
	private static BlackListTweetRemoval instance;
	
	private Set<String> blackListedWords = new HashSet<>();
	public static synchronized BlackListTweetRemoval getInstance() throws SQLException{
		if(instance == null){
			instance = new BlackListTweetRemoval();
			instance.blackListedWords = instance.fetchBlackListedWords();
		}
		return instance;
	}
	
	private BlackListTweetRemoval(){}
	
	
	public Set<String> fetchBlackListedWords() throws SQLException{
		Set<String> blackListedWords = new HashSet<>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from blacklist");
		while (resultSet.next()) {
			String word = resultSet.getString("blacklistword");
			blackListedWords.add(word);
		}
		DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		return blackListedWords;
	}
	
	
	public static void main(String args[]) throws SQLException{
		Tweet tweet = new Tweet();
		tweet.setTweetStr("porn kuch bhi");
		BlackListTweetRemoval.getInstance().processTweets(Arrays.asList(tweet));
	}

	@Override
	public void processTweets(List<Tweet> tweets) {
		for (Tweet tweet : tweets) {
			String[] tokens = tweet.getTweetStr().split(" ");
			
			for (String str : tokens) {
				if (blackListedWords.contains(str.toLowerCase())) {
					tweet.setStatus("spam");
					break;
				}
			}
		}
	}
}
