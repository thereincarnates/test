package com.movieztalk.wikipedia;

import java.io.IOException;

import javax.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

public class WikipediaExtractor {

	public String fetchPlotString(@Nullable String url) throws IOException {
		if(Strings.isNullOrEmpty(url)){
			return "";
		}
		String plot = "";

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
			if (e.id().toLowerCase().contains("plot") || e.id().toLowerCase().contains("synopsis")) {
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
			}
		}

		return plot.replaceAll("\\[\\d\\]", "").trim();
	}

	public static void main(String args[]) throws IOException {
		System.out.println(new WikipediaExtractor().fetchPlotString("https://en.wikipedia.org/wiki/Saansein	"));
	}

}
