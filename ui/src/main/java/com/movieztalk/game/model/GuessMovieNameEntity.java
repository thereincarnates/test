package com.movieztalk.game.model;

public class GuessMovieNameEntity {
	private String playerId;
	private String movieName;
	private String score;
	private String otherPlayerId;
	private String otherPlayerScore;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getOtherPlayerId() {
		return otherPlayerId;
	}

	public void setOtherPlayerId(String otherPlayerId) {
		this.otherPlayerId = otherPlayerId;
	}

	public String getOtherPlayerScore() {
		return otherPlayerScore;
	}

	public void setOtherPlayerScore(String otherPlayerScore) {
		this.otherPlayerScore = otherPlayerScore;
	}
}
