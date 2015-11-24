package com.movieztalk.game.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {

  private PlayerScoreBoard playerScoreBoard1;
  private PlayerScoreBoard playerScoreBoard2;

  public PlayerScoreBoard getPlayerScoreBoard1() {
    return playerScoreBoard1;
  }

  public void setPlayerScoreBoard1(PlayerScoreBoard playerScoreBoard1) {
    this.playerScoreBoard1 = playerScoreBoard1;
  }

  public PlayerScoreBoard getPlayerScoreBoard2() {
    return playerScoreBoard2;
  }

  public void setPlayerScoreBoard2(PlayerScoreBoard playerScoreBoard2) {
    this.playerScoreBoard2 = playerScoreBoard2;
  }

  public class PlayerScoreBoard {
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

}
