package com.movieztalk.spamremoval;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.core.Instance;

public class URLSpamRemoval {

	private static Pattern urlPattern = Pattern.compile(".*http.*");

	private static URLSpamRemoval instance = null;

	public static synchronized URLSpamRemoval getInstance() {
		if (instance == null) {
			instance = new URLSpamRemoval();
		}
		return instance;
	}

	public boolean isSpam(String tweet) {
		Matcher matcher = urlPattern.matcher(checkNotNull(tweet));
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String x = "fvfjvjfdhtp";
		System.out.println(new URLSpamRemoval().isSpam(x));
	}
}
