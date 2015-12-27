package com.movieztalk.game.builder;

import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.util.DataStoreUtil;
import com.google.api.services.datastore.client.DatastoreException;

public class GuessMovieNameGameBuilder {

  private final GuessMovieNameGame guessMovieNameGame;

  public GuessMovieNameGameBuilder() {
    guessMovieNameGame = new GuessMovieNameGame();
  }

  public GuessMovieNameGame build() {
   /* Map<String, String> propertyMap = new HashMap<>();
    propertyMap.put("initiatorid", guessMovieNameGame.getInitiatorId());
    propertyMap.put("otherplayerid", guessMovieNameGame.getOtherPlayerId());
    propertyMap.put("gamecurrentstate", guessMovieNameGame.getGameCurrentState());
    propertyMap.put("moviename", guessMovieNameGame.getMovieName());
    propertyMap.put("scoreboard", "");
    DataStoreHelper.storeData(propertyMap, "gamestate");*/
    return guessMovieNameGame;
  }

  public GuessMovieNameGameBuilder buildGameState() {
    guessMovieNameGame.setGameCurrentState("");
    return this;
  }

  public GuessMovieNameGameBuilder buildInitiatorId() throws DatastoreException {
    String initiatorId = DataStoreUtil.buildUniqueId();
    guessMovieNameGame.setInitiatorId(initiatorId);
    return this;
  }

  public GuessMovieNameGameBuilder buildMovieName() {
    guessMovieNameGame.setMovieName("");
    return this;
  }

  public GuessMovieNameGameBuilder buildOtherPlayerId() throws DatastoreException {
    String otherPlayerId = DataStoreUtil.buildUniqueId();
    guessMovieNameGame.setOtherPlayerId(otherPlayerId);
    return this;
  }
}
