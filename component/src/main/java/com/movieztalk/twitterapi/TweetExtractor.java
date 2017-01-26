package com.movieztalk.twitterapi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.movieztalk.UUID.UUIDGenerator;
import com.movieztalk.cosinesimilarity.CosineSimilarityOnWords;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Tweet;
import com.movieztalk.spamremoval.URLSpamRemoval;
import com.mysql.cj.api.x.Result;

import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TweetExtractor {

	public static int BATCH_SIZE = 10;

	public static void main(String[] args) throws TwitterException, IOException {
		final List<Tweet> tweets = new ArrayList<>();
		StatusListener listener = new StatusListener() {

			public void onStatus(Status status) {
				
				Tweet tweet = new Tweet();
				tweet.setTweetId(Long.toString(status.getId())).setTweetStr(status.getText());
				if (tweets.size() < BATCH_SIZE) {
					if (!URLSpamRemoval.getInstance().isSpam(tweet.getTweetStr()) && !status.isRetweet()) {
						tweets.add(tweet);
						System.out.println(status.getText());
					}
				} else {
					List<Tweet> cosinedTweets = CosineSimilarityOnWords.getInstance().getNonRepeatingTweets(tweets);
					List<Tweet> clonedList = new ArrayList<>(cosinedTweets);
					addTaskIdIntoTweets(clonedList);
					DatabaseHelper.getInstance().storeTaskIdInDB(clonedList.get(0).getTaskid());
					DatabaseHelper.getInstance().storeTweetsInDB(clonedList);
					tweets.clear();
					cosinedTweets.clear();
				}
				
			}

			private void addTaskIdIntoTweets(List<Tweet> clonedList) {
				String taskId = UUIDGenerator.getInstance().fetchUUID();
				for (Tweet tweet : clonedList) {
					tweet.setTaskid(taskId);
				}
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}
		};

		final String CONSUMER_KEY = "x5GQW0xsygry4cCZd1q0VPWHk";
		final String CONSUMER_KEY_SECRET = "G9aOGgceg73PPP42k8yaaVcSArSb0inKmwdBJRplxHbIR3m2fZ";
		String accessToken = "3224462576-hMMUlHapx6cniQvtvPMqnEYl7c7BJMOs9LCPPiy";
		String accessTokenSecret = "w7AzANLQELEGo4dI7l8e8ucg4j3DMozy3u9DxT2pbTNRh";

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);

		RequestToken requestToken = twitterStream.getOAuthRequestToken();
		System.out.println("Authorization URL: \n" + requestToken.getAuthorizationURL());
		twitterStream.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

		twitterStream.addListener(listener);
		FilterQuery fquery = new FilterQuery();
		String keywords[] = getTweetHashWordsForMoviesInProcessing();

		fquery.track(keywords);

		twitterStream.filter(fquery);
	}

	private static String[] getTweetHashWordsForMoviesInProcessing() {
		List<String> result = new ArrayList<>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select hashtag from movie where currently_processed=\"Y\"");
			while (resultSet.next()) {
				String hashtag = resultSet.getString("hashtag");
				result.add(hashtag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return result.toArray(new String[result.size()]);
	}
}
