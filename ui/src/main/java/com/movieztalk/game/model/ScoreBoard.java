package com.movieztalk.game.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {

  private final List<Integer> scores = new ArrayList<>();
  private String totalScore;
  private String totalGamePlayed;

  public String getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(String totalScore) {
    this.totalScore = totalScore;
  }

  public String getTotalGamePlayed() {
    return totalGamePlayed;
  }

  public void setTotalGamePlayed(String totalGamePlayed) {
    this.totalGamePlayed = totalGamePlayed;
  }

  public List<Integer> getScores() {
    return scores;
  }

}
