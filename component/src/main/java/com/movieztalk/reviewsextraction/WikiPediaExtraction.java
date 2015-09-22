package com.movieztalk.reviewsextraction;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiPediaExtraction {
	public static void main(String args[]) throws IOException {
		File input = new File("/tmp/input.html");
		// Document doc=
		// Jsoup.parse("http://en.wikipedia.org/wiki/List_of_Telugu_films_of_2015",
		// "10000");

		Document doc = Jsoup
				.connect(
						"https://en.wikipedia.org/wiki/Advantageous")
				.get();
		Elements es = doc.getAllElements();
		boolean flag = false;
		for(Element e: es){
			//System.out.println(e.id());
			if(flag){
				if(e.tagName().equalsIgnoreCase("p")){
					System.out.println(e);
				}
			}
			if(flag){
				if(e.tagName().toLowerCase().contains("h2")){
					flag=false;
				}
			}
			if(e.id().toLowerCase().contains("plot")){
				flag = true;
				//System.out.println("macteh");
				//System.out.println(e);
			}

		}

		//System.out.println(doc);
		Elements resultLinks=doc.select(".infobox.vevent");
		//System.out.println(resultLinks);
		//Elements resultLinks = doc.getElementsByAttributeValueContaining("class", "infobox venevt");
		//doc.getElementsByClass("infobox vevent");

/*		Element x = doc.getElementById("infobox vevent");
		System.out.println(x);
*/
																													// a
																													// after

		//System.out.println(resultLinks.get(0));
		// h3
		// System.out.println(doc);

		for (int i = 0; i < resultLinks.size(); i++) {
			System.out.println(resultLinks.get(i));
		}

	}
}
