package com.movieztalk.spamremoval;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLSpamRemoval {

	private static Pattern urlPattern = Pattern.compile(".*http.*");

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
