package com.movieztalk.game.model;

public class GuessMovieNameGame {

  private String gameCurrentState;
  private String initiatorId;
  private String otherPlayerId;
  private String movieName;

  public String getGameCurrentState() {
    return gameCurrentState;
  }

  public void setGameCurrentState(String gameCurrentState) {
    this.gameCurrentState = gameCurrentState;
  }

  public String getInitiatorId() {
    return initiatorId;
  }

  public void setInitiatorId(String initiatorId) {
    this.initiatorId = initiatorId;
  }

  public String getOtherPlayerId() {
    return otherPlayerId;
  }

  public void setOtherPlayerId(String otherPlayerId) {
    this.otherPlayerId = otherPlayerId;
  }

  public String getMovieName() {
    return movieName;
  }

  public void setMovieName(String movieName) {
    this.movieName = movieName;
  }

}
