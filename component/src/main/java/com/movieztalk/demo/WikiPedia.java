package com.movieztalk.demo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiPedia {

	public static void main(String args[]) throws IOException {
		String url = "https://en.wikipedia.org/wiki/Saansein";
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.getElementsByClass("infobox vevent");
		boolean directedFlag = false;
		for (Element element : elements) {
			System.out.println(element.tagName());
			if (directedFlag) {
				System.out.println(element.text());
				directedFlag = false;
			}
			if (element.tagName().equalsIgnoreCase("th")) {
				System.out.println(element.text());
				if (element.text().toLowerCase().contains("directed")) {
					System.out.println(element.text());
					directedFlag = true;
				}
			}
		}
	}

}
