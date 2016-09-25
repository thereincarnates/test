package com.movieztalk;

import java.util.List;

import com.movieztalk.extraction.model.Tweet;

public interface TweetProcessor {
	public void processTweets(List<Tweet> tweets);
}
