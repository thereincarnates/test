package com.movieztalk.game.model;

public class MovieRequest {

  private String moviename;
  private ScoreBoard scoreboard;

  public String getMoviename() {
    return moviename;
  }

  public void setMoviename(String moviename) {
    this.moviename = moviename;
  }

  public ScoreBoard getScoreboard() {
    return scoreboard;
  }

  public void setScoreboard(ScoreBoard scoreboard) {
    this.scoreboard = scoreboard;
  }

}
