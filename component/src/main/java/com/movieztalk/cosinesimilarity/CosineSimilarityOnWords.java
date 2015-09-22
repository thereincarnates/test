package com.movieztalk.cosinesimilarity;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.simmetrics.StringMetricBuilder.with;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Case;
import org.simmetrics.tokenizers.Whitespace;

import twitter4j.Status;

public class CosineSimilarityOnWords implements ReviewsSimilarity {

	private static double COSINE_THRESHOLD = 0.8;

	@Override
	public double findCosineSimilarity(String sentence1, String sentence2) {

		StringMetric metric = with(new CosineSimilarity<String>())
				.simplify(new Case.Lower(Locale.ENGLISH))
				.tokenize(new Whitespace())
				.build();
		return metric.compare(checkNotNull(sentence1), checkNotNull(sentence2));
	}

	public List<Status> getNonRepeatingTweets(List<Status> tweets) {
		Set<Integer> indexToRemove = new HashSet<>();
		for(int i=0;i<tweets.size();i++){
			for(int j=i+1;j<tweets.size();j++){
				if(!indexToRemove.contains(i)
					&& !indexToRemove.contains(j)
					&& findCosineSimilarity(tweets.get(i).getText(), tweets.get(j).getText())>COSINE_THRESHOLD){
					indexToRemove.add(j);
					System.out.println("Removing" + tweets.get(i).getText()+"\t" + tweets.get(j).getText());
				}
			}
		}
		for(int index : indexToRemove){
			tweets.remove(index);
		}
		return tweets;
	}

	public static void main(String args[]){
		ReviewsSimilarity similarity = new CosineSimilarityOnWords();
		System.out.println(similarity.findCosineSimilarity("This is super", "This IS again super"));
	}

}
