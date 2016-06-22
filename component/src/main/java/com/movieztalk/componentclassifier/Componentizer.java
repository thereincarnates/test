package com.movieztalk.componentclassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.movieztalk.yaml.Configuration;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class Componentizer {
	/**
	 * Main method which calls all the methods to perform steps for complete classification of tweet
	 * 1. Build the component dictionary from yaml file
	 * 2. Tokenize the input tweet to get the words
	 * 3. Classify the tweet into one or more of the component classes
	 * 
	 * @param ar
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static void main(String ar[]) throws InvalidFormatException, IOException
	{
	   ComponentDictionary componentDict = PopulateComponentDictionary();
	   String tweet = "Direction, Story is good";
	   String[] tweetToken = Tokenize(tweet);
	   HashSet<String> compClass = null;
	   
	   ComponentClassifier classifier = new ComponentClassifier();
	   if(componentDict != null)
	   {
	      compClass = classifier.classify(componentDict, tweetToken);	 
	   }
	   else
		  System.err.print("Check the component Dictionary is empty , componentDict: " + componentDict);
	   
	   
	   System.out.println("Tweet belongs to below components: ");
	   for(String comp: compClass)
	   {
		   System.out.println(comp);
	   }
	  
	}
	
	/**
	 * Populating the component Dictionary using yaml file
	 */
	public static ComponentDictionary PopulateComponentDictionary(){
		 Yaml yaml = new Yaml(); 
		 System.out.println("Populating the dictionary and printing the details of componentDictionary");
		 ComponentDictionary compDict = null;
		 
	     File componentYamlFile = new File("src/main/java/com/movieztalk/componentclassifier/component.yml");
	     
	     try( InputStream in = new FileInputStream(componentYamlFile) ) {
	            compDict = yaml.loadAs( in, ComponentDictionary.class );
	            System.out.println( compDict.toString());
	     }
	     catch(Exception e){
	    	 System.out.println("Exception: "+ e); 
	     }
	   
	     return compDict; 
	}
	
	/**
	 * Tokenize the input string which contains tweets
	 * 
	 * @param tweet
	 * @return String[]
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static String[] Tokenize(String tweet) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("modelfiles/en-token.bin");
	 
		TokenizerModel model = new TokenizerModel(is);
	 
		Tokenizer tokenizer = new TokenizerME(model);
	 
		String tokens[] = tokenizer.tokenize(tweet);
	    
		for (String a : tokens)
		{
			//System.out.println(a);
		}
		is.close();
		
		return tokens;
	}
}
