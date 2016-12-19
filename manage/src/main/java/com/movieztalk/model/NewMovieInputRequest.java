package com.movieztalk.model;

public class NewMovieInputRequest {
	private String name;
	private String hashTag;
	private String wikiUrl;
	private String songsAndTrailers;
	private String videoReviews;
	private String interviewsAndEvents;
	private String id;
	private String director;
	private String writers;
	private String actors;
	private String boxOffice;
	private String budget;
	private String releaseDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getWikiUrl() {
		return wikiUrl;
	}

	public void setWikiUrl(String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}

	public String getSongsAndTrailers() {
		return songsAndTrailers;
	}

	public void setSongsAndTrailers(String songsAndTrailers) {
		this.songsAndTrailers = songsAndTrailers;
	}

	public String getVideoReviews() {
		return videoReviews;
	}

	public void setVideoReviews(String videoReviews) {
		this.videoReviews = videoReviews;
	}

	public String getInterviewsAndEvents() {
		return interviewsAndEvents;
	}

	public void setInterviewsAndEvents(String interviewsAndEvents) {
		this.interviewsAndEvents = interviewsAndEvents;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getBoxOffice() {
		return boxOffice;
	}

	public void setBoxOffice(String boxOffice) {
		this.boxOffice = boxOffice;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
}
