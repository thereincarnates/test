package com.movieztalk.wikipedia;

import java.util.List;

public class MoviesMetaData {

	private String name;
	private List<String> directors;
	private List<String> actors;
	private String releaseDate;
	private String boxOfficeCollection;
	private String budget;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDirectors() {
		return directors;
	}

	public void setDirectors(List<String> directors) {
		this.directors = directors;
	}

	public List<String> getActors() {
		return actors;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getBoxOfficeCollection() {
		return boxOfficeCollection;
	}

	public void setBoxOfficeCollection(String boxOfficeCollection) {
		this.boxOfficeCollection = boxOfficeCollection;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

}
