package com.movieztalk.wikipedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.common.base.Strings;

public class WikipediaExtractor {

	private static final Gson gson = new Gson();

	public String fetchPlotString(@Nullable String url) throws IOException {
		if (Strings.isNullOrEmpty(url)) {
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

	public MoviesMetaData fetchMovieMetaData(@Nullable String url) throws IOException {

		if (Strings.isNullOrEmpty(url)) {
			return null;
		}

		MoviesMetaData metaData = new MoviesMetaData();

		Document doc = Jsoup.connect(url).get();
		Elements es = doc.select("table[class=infobox vevent]");
		Iterator<Element> it = es.select("tr").iterator();
		boolean nameSet = false;

		while (it.hasNext()) {

			Element e = it.next();
			String name = e.select("th[class=summary]").text();
			if (!nameSet) {
				nameSet = true;
				metaData.setName(name);
			}
			String th = e.select("th[scope=row]").text();
			switch (th.toLowerCase()) {
			case "directed by":
				metaData.setDirectors(convertToList(e.select("a[href]")));
				break;
			case "starring":
				metaData.setActors(convertToList(e.select("a[href]")));
				break;
			case "produced by":
				break;
			case "release date":
				Elements list = e.select("li");
				Element date = list.get(0);
				Elements toRemove = date.select("span, sup");
				for (Element temp : toRemove) {
					temp.remove();
				}
				String releaseDate = date.text();
				metaData.setReleaseDate(releaseDate);
				break;
			case "budget":
				String budget = e.select("td").text();
				budget = budget.replaceAll("^[^\\d]*|\\[[\\d]*\\]", "");
				metaData.setBudget(budget);
				break;
			case "box office":
				String boxOffice = e.select("td").text();
				boxOffice = boxOffice.replaceAll("^[^\\d]*|\\[[\\d]*\\]", "");
				metaData.setBoxOfficeCollection(boxOffice);
				break;
			default:
				continue;
			}

		}
		return metaData;
	}

	public static void main(String args[]) throws IOException {
		MoviesMetaData metaData = new WikipediaExtractor().fetchMovieMetaData("https://en.wikipedia.org/wiki/Kaabil");
		System.out.println(gson.toJson(metaData));
	}

	private List<String> convertToList(Elements elements) {

		if (null == elements)
			return null;

		List<String> elList = new ArrayList<>();

		Iterator<Element> ed = elements.select("a[href]").iterator();
		while (ed.hasNext()) {
			elList.add(ed.next().text());
		}

		return elList;
	}

}
