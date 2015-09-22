package com.movieztalk.extraction.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.movieztalk.movieaspects.model.MovieAspect;

/**
 * Movie represents each movie along with its entities
 */
public class Movie {
  // Name of the movie
  private String name;
  // Name of the director
  private Celebrity director;
  // Budget of the movie
  private String budget;
  // Box office of the movie
  private String boxOffice;
  // Actors and actresses in movie
  private final Set<Celebrity> actors = new HashSet<>();
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
  private final Set<String> songAndTrailers = new HashSet<>();
  // Embeded URLs for video reviews.
  private final Set<String> videoReviews = new HashSet<>();
  // Embeded URLs for interviews and events
  private final Set<String> interviewAndEvents = new HashSet<>();
  // Plot of movie
  private String plot;
  // Celebrity tweets
  private final Set<String> tweets = new LinkedHashSet<>();

  public String getPlot() {
    return plot;
  }
  public void setPlot(String plot) {
    this.plot = plot;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Celebrity getDirector() {
    return director;
  }
  public void setDirector(Celebrity director) {
    this.director = director;
  }
  public String getBudget() {
    return budget;
  }
  public void setBudget(String budget) {
    this.budget = budget;
  }
  public String getBoxOffice() {
    return boxOffice;
  }
  public void setBoxOffice(String boxOffice) {
    this.boxOffice = boxOffice;
  }
  public MovieAspect getOverall() {
    return overall;
  }
  public void setOverall(MovieAspect overall) {
    this.overall = overall;
  }
  public MovieAspect getStory() {
    return story;
  }
  public void setStory(MovieAspect story) {
    this.story = story;
  }
  public MovieAspect getActing() {
    return acting;
  }
  public void setActing(MovieAspect acting) {
    this.acting = acting;
  }
  public MovieAspect getMusic() {
    return music;
  }
  public void setMusic(MovieAspect music) {
    this.music = music;
  }
  public MovieAspect getDirection() {
    return direction;
  }
  public void setDirection(MovieAspect direction) {
    this.direction = direction;
  }
  public Set<Celebrity> getActors() {
    return actors;
  }
  public Set<String> getSongAndTrailers() {
    return songAndTrailers;
  }
  public Set<String> getVideoReviews() {
    return videoReviews;
  }
  public Set<String> getInterviewAndEvents() {
    return interviewAndEvents;
  }
}
