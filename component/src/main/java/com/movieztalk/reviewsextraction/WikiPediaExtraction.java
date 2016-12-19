package com.movieztalk.reviewsextraction;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

public class WikiPediaExtraction {
	public static void main(String args[]) throws IOException {
		String plot = null;

		String url = "https://en.wikipedia.org/wiki/Moh_Maya_Money";
		Document doc = Jsoup.connect(url).get();
		Elements es = doc.getAllElements();
		boolean flag = false;
		for (Element e : es) {
			// System.out.println(e.id());
			if (flag) {
				if (e.tagName().equalsIgnoreCase("p")) {
					plot = plot + "\n" + Jsoup.parse(e.toString()).text();
				}
			}
			if (flag) {
				if (e.tagName().toLowerCase().contains("h2")) {
					flag = false;
				}
			}
			if (e.id().toLowerCase().contains("plot")) {
				flag = true;
			}
		}

		if (Strings.isNullOrEmpty(plot)) {
			boolean randomFlag = false;
			for (Element element : es) {
				if (randomFlag) {
					if (element.tagName().equalsIgnoreCase("p")) {
						plot = Jsoup.parse(element.toString()).text();
						randomFlag = false;
					}
				}
				if (element.id().equalsIgnoreCase("mw-content-text")) {
					randomFlag = true;
				}
				// System.out.println(element.tagName() + "\t" + element.id());
			}
		}

		System.out.println(plot.replaceAll("\\[\\d\\]", "").trim());

		// System.out.println(doc);
		// Elements resultLinks = doc.select(".infobox.vevent");
		// System.out.println(resultLinks);
		// Elements resultLinks =
		// doc.getElementsByAttributeValueContaining("class", "infobox venevt");
		// doc.getElementsByClass("infobox vevent");

		/*
		 * Element x = doc.getElementById("infobox vevent");
		 * System.out.println(x);
		 */

	}
}
