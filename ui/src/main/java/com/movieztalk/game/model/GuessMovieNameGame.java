package com.movieztalk.game.model;

import com.movieztalk.server.MovieGameInitializerServlet;

public class GuessMovieNameGame {

  private String gameState;
  private int initiatorId;
  private int otherPlayerId;

  public String getGameState() {
    return gameState;
  }

  public void setGameState(String gameState) {
    this.gameState = gameState;
  }

  public int getInitiatorId() {
    return initiatorId;
  }

  public void setInitiatorId(int initiatorId) {
    this.initiatorId = initiatorId;
  }

  public int getOtherPlayerId() {
    return otherPlayerId;
  }

  public void setOtherPlayerId(int otherPlayerId) {
    this.otherPlayerId = otherPlayerId;
  }

  public static GuessMovieNameGame build() {
    GuessMovieNameGame obj = new GuessMovieNameGame();
    obj.setInitiatorId(getURLId());
    obj.setOtherPlayerId(getURLId());
    obj.setGameState("");
    return obj;
  }

  public static int getURLId() {
    while (true) {
      int probableUrl = Math.abs(String.valueOf(System.currentTimeMillis() * Math.random())
          .hashCode());
      if (!MovieGameInitializerServlet.usedURLs.contains(probableUrl)) {
        MovieGameInitializerServlet.usedURLs.add(probableUrl);
        return probableUrl;
      }
    }
  }
}
