package com.movieztalk.demo;

import java.util.ArrayList;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterStreamDemo {

	private static final String CONSUMER_KEY = "x5GQW0xsygry4cCZd1q0VPWHk";
	private static final String CONSUMER_KEY_SECRET = "G9aOGgceg73PPP42k8yaaVcSArSb0inKmwdBJRplxHbIR3m2fZ";
	private static String accessToken = "3224462576-hMMUlHapx6cniQvtvPMqnEYl7c7BJMOs9LCPPiy";
	private static String accessTokenSecret = "w7AzANLQELEGo4dI7l8e8ucg4j3DMozy3u9DxT2pbTNRh";

	public static void main(String args[]) throws TwitterException {

		TwitterStream twitter = TwitterStreamFactory.getSingleton();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
		RequestToken requestToken = twitter.getOAuthRequestToken();
		twitter.setOAuthAccessToken(new AccessToken(accessToken,
				accessTokenSecret));

		StatusListener listener = new StatusListener() {

			final List<Status> statusList = new ArrayList<Status>();

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStatus(Status status) {
				System.out.println(status);
				if (status.isRetweet()) {
					return;
				}

				statusList.add(status);
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub
			}

		};

		twitter.addListener(listener);
		FilterQuery fq = new FilterQuery();
		String[] searchTerms = { "#dilwale", "#tamasha", "#prtp" };
		String[] language = { "en" };
		fq.track(searchTerms);
		fq.language(language);
		twitter.filter(fq);
	}
}
