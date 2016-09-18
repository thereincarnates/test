package com.movieztalk.extraction.model;

public class Tweet {
	private String tweetId;
	private String tweetStr;
	private String status;
	private String movieId;
	private String compName;
	private String sentiment;
	private String taskid;

	public String getTweetId() {
		return tweetId;
	}

	public Tweet setTweetId(String tweetId) {
		this.tweetId = tweetId;
		return this;
	}

	public String getTweetStr() {
		return tweetStr;
	}

	public Tweet setTweetStr(String tweetStr) {
		this.tweetStr = tweetStr;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public Tweet setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getMovieId() {
		return movieId;
	}

	public Tweet setMovieId(String movieId) {
		this.movieId = movieId;
		return this;
	}

	public String getCompName() {
		return compName;
	}

	public Tweet setCompName(String compName) {
		this.compName = compName;
		return this;
	}

	public String getSentiment() {
		return sentiment;
	}

	public Tweet setSentiment(String sentiment) {
		this.sentiment = sentiment;
		return this;
	}

	public String getTaskid() {
		return taskid;
	}

	public Tweet setTaskid(String taskid) {
		this.taskid = taskid;
		return this;
	}
}
