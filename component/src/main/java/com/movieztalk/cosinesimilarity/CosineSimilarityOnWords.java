package com.movieztalk.cosinesimilarity;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.simmetrics.StringMetricBuilder.with;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Case;
import org.simmetrics.tokenizers.Whitespace;

import com.movieztalk.extraction.model.Tweet;

import twitter4j.Status;

public class CosineSimilarityOnWords implements ReviewsSimilarity {

	private static double COSINE_THRESHOLD = 0.8;

	private static CosineSimilarityOnWords instance = null;

	public static synchronized CosineSimilarityOnWords getInstance() {
		if (instance == null) {
			instance = new CosineSimilarityOnWords();
		}
		return instance;
	}

	@Override
	public double findCosineSimilarity(String sentence1, String sentence2) {

		StringMetric metric = with(new CosineSimilarity<String>()).simplify(new Case.Lower(Locale.ENGLISH))
				.tokenize(new Whitespace()).build();
		return metric.compare(checkNotNull(sentence1), checkNotNull(sentence2));
	}

	public List<Tweet> getNonRepeatingTweets(List<Tweet> tweets) {
		Set<Integer> indexToRemove = new HashSet<>();
		for (int i = 0; i < tweets.size(); i++) {
			for (int j = i + 1; j < tweets.size(); j++) {
				if (findCosineSimilarity(tweets.get(i).getTweetStr(),
						tweets.get(j).getTweetStr()) >= COSINE_THRESHOLD) {
					indexToRemove.add(j);
				}
			}
		}

		int i = 0;
		Iterator<Tweet> itr = tweets.iterator();
		while (itr.hasNext()) {
			itr.next();
			if (indexToRemove.contains(i)) {
				itr.remove();
			}
			i++;
		}

		return tweets;
	}

	public static void main(String args[]) {
		ReviewsSimilarity similarity = new CosineSimilarityOnWords();
		System.out.println(similarity.findCosineSimilarity("This is super", "This IS again super"));
	}
}
