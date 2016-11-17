package com.movieztalk.model;

public class NewMovieInputRequest {
	private String name;
	private String hashTag;
	private String wikiUrl;
	private String songsAndTrailers;
	private String videoReviews;
	private String interviewsAndEvents;

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

	@Override
	public String toString() {
		return "NewMovieInputRequest [name=" + name + ", hashTag=" + hashTag + ", wikiUrl=" + wikiUrl
				+ ", songsAndTrailers=" + songsAndTrailers + ", videoReviews=" + videoReviews + ", interviewsAndEvents="
				+ interviewsAndEvents + "]";
	}
	
	

}
