package com.movieztallk.movieclassifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.movieztalk.TweetProcessor;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Tweet;
import com.movieztalk.helper.RegexHelper;

public class MovieClassifier implements TweetProcessor{

	private RegexHelper regexHelper = RegexHelper.getInstance();
	private DatabaseHelper dbHelper = DatabaseHelper.getInstance();

	private static MovieClassifier instance;

	private Multimap<String, String> movieIdToKeyWordsMapping;

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	private MovieClassifier() {
	}

	public static  synchronized MovieClassifier getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new MovieClassifier();
			instance.populateMovieNameToKeyWordMap();
		}
		return instance;
	}

	private void populateMovieNameToKeyWordMap() throws ClassNotFoundException, SQLException {
		instance.movieIdToKeyWordsMapping = HashMultimap.create();
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from movieclassification");
		while (resultSet.next()) {
			String movieId = resultSet.getString("movieid");
			instance.movieIdToKeyWordsMapping.putAll(movieId,
					Arrays.asList(regexHelper.commaPattern.split(resultSet.getString("moviekeywords"))));
		}
		dbHelper.closeResources(connect, statement, resultSet);
	}

	@Override
	public void processTweets(List<Tweet> tweets) {
		Preconditions.checkNotNull(tweets);
		for (Tweet tweet : tweets) {
			populateMovieForTweet(tweet);
		}
	}

	private void populateMovieForTweet(Tweet tweet) {
		String tweetStr = tweet.getTweetStr();
		if (!Strings.isNullOrEmpty(tweetStr)) {
			for (String token : regexHelper.spacePattern.split(tweetStr)) {
				for (String movieId : movieIdToKeyWordsMapping.keys()) {
					if (movieIdToKeyWordsMapping.get(movieId).contains(token)) {
						tweet.setMovieId(movieId);
						return;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Tweet tweet = new Tweet();
		tweet.setTweetStr("kya baat hai #pink");
		new MovieClassifier().getInstance().processTweets(Arrays.asList(tweet));
		System.out.println("movie name " + tweet.getMovieId());
	}
}
