package com.movieztallk.movieclassifier;

public class MovieClassifierEntity {
	private int movieId;
	private String movieName;
	private String movieKeyWords;

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getMovieKeyWords() {
		return movieKeyWords;
	}

	public void setMovieKeyWords(String movieKeyWords) {
		this.movieKeyWords = movieKeyWords;
	}
}
