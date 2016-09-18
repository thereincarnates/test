package com.movieztallk.movieclassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class MovieClassifier {
	
	public static void main(String[] args) {
		HashMap<String,List<String>> hashTagMap = new HashMap<>();
		List<String> tweetList= new ArrayList<>();
		
	    tweetList.add("#Pink...Thought provoking. Applause to Pawan Kumar and Rashmi " +
	    		       "Sharma to debut successfully with such a sensitive subject.");
	    
	    tweetList.add("RT @IndianExpress: PINK boxoffice day 2: @SrBachchan-starrer earns Rs 11.97 cr," +
	    		      " witnesses remarkable growth");
	    
	    tweetList.add("@SrBachchan-starrer earns Rs 11.97 cr," +
	    		      " witnesses remarkable growth");
	    
	    List<String> hashTag = new ArrayList<>();
	    hashTag.add("pink");
	    hashTag.add("#Pink");
	    
        hashTagMap.put("Pink", hashTag);
        
		MovieClassifier movieClassifier = new MovieClassifier();
		List<String> movieList = movieClassifier.ClassifyTweetToMovie(hashTagMap, tweetList);	
        for(String movie: movieList)
        {
        	System.out.println("Printing Movie name :" + movie);
        }
	}
	
	public List<String> ClassifyTweetToMovie(HashMap<String,List<String>> hashTagMap, List<String> tweetList)
	{
		List<String> movieList = new ArrayList<>();
		for(String tweet: tweetList)
		{
			boolean valueExist = false;
			for (Entry<String, List<String>> tagEntry : hashTagMap.entrySet()) {
				List<String> tweetValues = tagEntry.getValue();
				for(String tweetVal : tweetValues)
				{
				    valueExist = tweet.toLowerCase().contains(tweetVal.toLowerCase());
					movieList.add(tagEntry.getKey());
				    break;
				}
				if(valueExist == true)
				{
					break;
				}
			}
			if(valueExist == false)
			{
				movieList.add("unknown");
			}
		}
		return movieList;
	}

}
