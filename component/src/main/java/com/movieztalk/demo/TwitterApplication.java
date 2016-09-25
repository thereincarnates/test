package com.movieztalk.demo;

import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;






import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.movieztalk.cosinesimilarity.CosineSimilarityOnWords;


public class TwitterApplication {

    public static void main(String[] args) throws InterruptedException, IOException {
    	 final String CONSUMER_KEY = "x5GQW0xsygry4cCZd1q0VPWHk";
    	 final String CONSUMER_KEY_SECRET ="G9aOGgceg73PPP42k8yaaVcSArSb0inKmwdBJRplxHbIR3m2fZ";
    	 String accessToken = "3224462576-hMMUlHapx6cniQvtvPMqnEYl7c7BJMOs9LCPPiy";
    	 String accessTokenSecret = "w7AzANLQELEGo4dI7l8e8ucg4j3DMozy3u9DxT2pbTNRh";

       /* if (args.length < 1) {
            System.out.println("java twitter4j.examples.search.SearchTweets [query]");
            System.exit(-1);
        }*/
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);

        try {
        	RequestToken requestToken = twitter.getOAuthRequestToken();
        	/*System.out.println("Authorization URL: \n"
        			  + requestToken.getAuthorizationURL());*/
        	 twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));


        	/* File file = new File("/Users/tarunagr/Desktop/a.txt");

        	 if (!file.exists()) {
 				file.createNewFile();
 			}*/
        	 /*FileWriter fw = new FileWriter(file.getAbsoluteFile());
 			BufferedWriter bw = new BufferedWriter(fw);*/

            Query query = new Query("#bhalebhalemagadivoy");
            QueryResult result;
            int counter = 0;
            int tweetcount = 0;
            List<Status> tweets = new ArrayList<>();
            do {
                result = twitter.search(query);
                tweets.addAll(result.getTweets());
                counter++;
                tweetcount = tweets.size();


                for (Status tweet : tweets) {
                    //System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                	System.out.println(tweet.getText()+ "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n"+ counter + " " + tweetcount);
                	System.out.println(tweet.toString());
                	tweetcount++;
                }
                counter++;
                if(counter>=180){
                	Thread.sleep(15*60*1000);
                	counter = 0;
                }
                tweetcount++;
            } while ((query = result.nextQuery()) != null && tweetcount<1000 && counter<180);
            System.out.println("Size Before the cosine similarity" + tweets.size());
            //tweets = new CosineSimilarityOnWords().getNonRepeatingTweets(tweets);
//            System.out.println("Size After the cosine similarity" + tweets.size());
            // bw.close();
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}