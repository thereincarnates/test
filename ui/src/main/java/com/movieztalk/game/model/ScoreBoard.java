package com.movieztalk.game.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {

  private PlayerScoreBoard playerScoreBoard1;
  private PlayerScoreBoard playerScoreBoard2;

  public ScoreBoard() {
    playerScoreBoard1 = new PlayerScoreBoard();
    playerScoreBoard2 = new PlayerScoreBoard();
  }

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
    private final List<Integer> scores;
    private int totalScore;
    private int totalGamePlayed;

    public PlayerScoreBoard() {
      totalScore = 0;
      totalGamePlayed = 0;
      scores = new ArrayList<>();
    }

    public int getTotalScore() {
      return totalScore;
    }

    public void setTotalScore(int totalScore) {
      this.totalScore = totalScore;
    }

    public int getTotalGamePlayed() {
      return totalGamePlayed;
    }

    public void setTotalGamePlayed(int totalGamePlayed) {
      this.totalGamePlayed = totalGamePlayed;
    }

    public List<Integer> getScores() {
      return scores;
    }

    @Override
    public String toString() {
      return "PlayerScoreBoard [scores=" + scores + ", totalScore=" + totalScore
          + ", totalGamePlayed=" + totalGamePlayed + "]";
    }
  }

  @Override
  public String toString() {
    return "ScoreBoard [playerScoreBoard1=" + playerScoreBoard1 + ", playerScoreBoard2="
        + playerScoreBoard2 + "]";
  }
}
