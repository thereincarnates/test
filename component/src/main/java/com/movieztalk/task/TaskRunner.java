package com.movieztalk.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Tweet;
import com.movieztalk.spamremoval.BlackListTweetRemoval;
import com.movieztallk.movieclassifier.MovieClassifier;

public class TaskRunner {

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public static void main(String[] args) throws SQLException, InterruptedException {
		TaskRunner runner = new TaskRunner();
		Set<String> taskIds = runner.fetchUnProcessedTasks();
		Set<Callable<String>> callables = new HashSet<Callable<String>>();
		for (String taskId : taskIds) {
			runner.addTaskToQueue(taskId, callables);
		}
		List<Future<String>> futures = runner.executorService.invokeAll(callables);
		Set<String> successFullTasks = new  HashSet<>();
		for(Future<String> future : futures){
			try {
				String task = future.get();
				successFullTasks.add(task);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		runner.updateTaskStatus(successFullTasks);
		System.out.println("successful tasks "+ successFullTasks);
		runner.executorService.shutdownNow();
	}
	
	private void updateTaskStatus(Set<String> successFullTasks) throws SQLException {
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try{
		connect = DriverManager
				.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
		preparedStatement = connect
				.prepareStatement("update taskid set status=? where taskid=?");
		for (String sucessfulTask : successFullTasks) {
			preparedStatement.setString(1, TaskState.DONE.name());
			preparedStatement.setString(2, sucessfulTask);
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		}finally{
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
	}

	private  void addTaskToQueue(final String taskId, Set<Callable<String>> callables) {
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<Tweet> tweets = fetchTweetFromTaskId(taskId);
				MovieClassifier.getInstance().processTweets(tweets);
				BlackListTweetRemoval.getInstance().processTweets(tweets);
				storeUpdatedTweets(tweets);
				return taskId;
			}
		});
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
			preparedStatement.setString(2, tweet.getCompName());
			preparedStatement.setString(3, tweet.getSentiment());
			preparedStatement.setInt(4, tweet.getRowId());
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		}finally{
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
	}

	private List<Tweet> fetchTweetFromTaskId(String taskId) throws SQLException {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Tweet> tweets = new ArrayList<>();
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from Tweet_Table where taskid = '" + taskId + "'");
			while (resultSet.next()) {
				Tweet tweet = new Tweet();
				tweet.setTweetId(resultSet.getString("tweetid")).setTweetStr(resultSet.getString("tweetstr"))
				.setRowId(resultSet.getInt("rowid"));
				tweets.add(tweet);
			}
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return tweets;
	}

	private Set<String> fetchUnProcessedTasks() throws SQLException {
		Set<String> unProcessedTasks = new HashSet<>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from taskid where status!='" + TaskState.DONE.name() + "'");
			while (resultSet.next()) {
				String taskId = resultSet.getString("taskid");
				unProcessedTasks.add(taskId);
			}
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return unProcessedTasks;
	}
}
