package com.movieztalk.twitterapi;

import java.io.IOException;

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

	public static void main(String[] args) throws TwitterException, IOException {
		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				System.out.println(status.toString());
			   System.out.println("\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
			}

			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
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
    	final String CONSUMER_KEY_SECRET ="G9aOGgceg73PPP42k8yaaVcSArSb0inKmwdBJRplxHbIR3m2fZ";
    	String accessToken = "3224462576-hMMUlHapx6cniQvtvPMqnEYl7c7BJMOs9LCPPiy";
    	String accessTokenSecret = "w7AzANLQELEGo4dI7l8e8ucg4j3DMozy3u9DxT2pbTNRh";
    	 
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
		
		RequestToken requestToken = twitterStream.getOAuthRequestToken();
    	System.out.println("Authorization URL: \n"
    			  + requestToken.getAuthorizationURL());
    	twitterStream.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

    	
		twitterStream.addListener(listener);
        FilterQuery fquery = new FilterQuery();
        String keywords[] = {"#pink"};

        fquery.track(keywords);
				
		// sample() method internally creates a thread which manipulates
		// TwitterStream and calls these adequate listener methods continuously.
		twitterStream.filter(fquery);
	}

}