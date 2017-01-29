package com.movieztalk.extraction.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.movieztalk.movieaspects.model.MovieAspect;

/**
 * Movie represents each movie along with its entities
 */
public class Movie {
	// Name of the movie
	private String name;
	// Name of the director
	private String director;
	// Budget of the movie
	private String budget;
	// Box office of the movie
	private String boxOffice;
	// Actors and actresses in movie
	private Set<String> actors = new HashSet<>();
	// Represents the overall aspect of the movie
	private MovieAspect overall;
	// Represents the story aspect of the movie
	private MovieAspect story;
	// Represents the acting aspect of the movie
	private MovieAspect acting;
	// Represents the music aspect of the movie
	private MovieAspect music;
	// Represents the direction aspect of the movie
	private MovieAspect direction;
	// Embeded URLs for songs and trailers of movie
	private final List<String> songAndTrailers = new ArrayList<>();
	// Embeded URLs for video reviews.
	private final List<String> videoReviews = new ArrayList<>();
	// Embeded URLs for interviews and events
	private final List<String> interviewAndEvents = new ArrayList<>();
	// Plot of movie
	private String plot;
	// Celebrity tweets
	private Set<String> tweets = new LinkedHashSet<>();
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public Movie setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		return this;
	}

	public String getPlot() {
		return plot;
	}

	public Movie setPlot(String plot) {
		this.plot = plot;
		return this;
	}

	public String getName() {
		return name;
	}

	public Movie setName(String name) {
		this.name = name;
		return this;
	}

	public String getDirector() {
		return director;
	}

	public Movie setDirector(String director) {
		this.director = director;
		return this;
	}

	public String getBudget() {
		return budget;
	}

	public Movie setBudget(String budget) {
		this.budget = budget;
		return this;
	}

	public String getBoxOffice() {
		return boxOffice;
	}

	public Movie setBoxOffice(String boxOffice) {
		this.boxOffice = boxOffice;
		return this;
	}

	public MovieAspect getOverall() {
		return overall;
	}

	public Movie setOverall(MovieAspect overall) {
		this.overall = overall;
		return this;
	}

	public MovieAspect getStory() {
		return story;
	}

	public Movie setStory(MovieAspect story) {
		this.story = story;
		return this;
	}

	public MovieAspect getActing() {
		return acting;
	}

	public Movie setActing(MovieAspect acting) {
		this.acting = acting;
		return this;
	}

	public MovieAspect getMusic() {
		return music;
	}

	public Movie setMusic(MovieAspect music) {
		this.music = music;
		return this;
	}

	public MovieAspect getDirection() {
		return direction;
	}

	public Movie setDirection(MovieAspect direction) {
		this.direction = direction;
		return this;
	}

	public List<String> getSongAndTrailers() {
		return songAndTrailers;
	}

	public List<String> getVideoReviews() {
		return videoReviews;
	}

	public Movie setVideoReviews(List<String> videoReviews) {
		this.videoReviews.addAll(videoReviews);
		return this;
	}

	public List<String> getInterviewAndEvents() {
		return interviewAndEvents;
	}

	public Set<String> getTweets() {
		return tweets;
	}

	public Movie setTweets(Set<String> tweets) {
		this.tweets = tweets;
		return this;
	}

	public Movie setSongAndTrailers(List<String> songAndTrailers) {
		this.songAndTrailers.addAll(songAndTrailers);
		return this;
	}

	public Movie setInterviewAndEvents(List<String> interviewAndEvents) {
		this.interviewAndEvents.addAll(interviewAndEvents);
		return this;
	}

	public Set<String> getActors() {
		return actors;
	}

	public Movie setActors(Set<String> actors) {
		this.actors = actors;
		return this;
	}
}
