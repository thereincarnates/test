package com.movieztalk.demo;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class OpenNlpDemo {

	public static void main(String args[]) {
		InputStream modelIn = OpenNlpDemo.class.getResourceAsStream("/en-ner-person.bin");
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(model);
			// String[] sentence = new String[] { "Pierre", "Vinken", "is",
			// "61", "years", "old", "." };
			String[] sentence = "@RanveerOfficial really loved #Befikre everytime you make us to fall in love you whenever you play a new role whether it's Bajirao or Dharam"
					.split(" ");
			Span nameSpans[] = nameFinder.find(sentence);
			System.out.println(nameSpans.length);
			for (Span span : nameSpans) {
				System.out.println(span);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
