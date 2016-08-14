package com.movieztalk.componentclassifier;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.movieztalk.util.NLPHelper;

import opennlp.tools.util.InvalidFormatException;

public class ComponentClassifier {

	private ComponentDictionary componentDictionary = ComponentDictionary.getInstance();

	public Set<String> classify(String tweet) throws InvalidFormatException, IOException {
		String[] tweetToken = NLPHelper.getOpenNLPTextTokenizer().
				tokenize(checkNotNull(tweet).toLowerCase());
		Set<String> componentClass = new HashSet<>();
		for (String token : tweetToken) {
			Iterator entries = componentDictionary.getComponentDictionary().entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				Set<String> wordList = (Set<String>) entry.getValue();
				boolean isPresent = wordList.contains(token);
				if (isPresent == true) {
					componentClass.add((String) entry.getKey());
				}
			}
		}
		return componentClass;
	}
	
	public static void main(String args[]) throws InvalidFormatException, IOException{
		System.out.println(new ComponentClassifier().classify("STORY"));
	}
}