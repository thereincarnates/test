package com.movieztalk.game.builder;

import static com.google.api.services.datastore.client.DatastoreHelper.*;

import java.util.HashMap;
import java.util.Map;

import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.helper.DataStoreHelper;
import com.google.api.services.datastore.DatastoreV1.GqlQuery;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;

public class GuessMovieNameGameBuilder {
  private final GuessMovieNameGame guessMovieNameGame;
  Datastore datastore = null;

  public GuessMovieNameGameBuilder() {
    guessMovieNameGame = new GuessMovieNameGame();
  }

  public GuessMovieNameGameBuilder buildGameState() {
    guessMovieNameGame.setGameCurrentState("");
    return this;
  }

  public GuessMovieNameGameBuilder buildInitiatorId() throws DatastoreException {
    String initiatorId = generateuniqueID("initiatorid");
    guessMovieNameGame.setInitiatorId(initiatorId);
    return this;
  }

  public GuessMovieNameGameBuilder buildOtherPlayerId() throws DatastoreException {
    String otherPlayerId = generateuniqueID("otherplayerid");
    guessMovieNameGame.setOtherPlayerId(otherPlayerId);
    return this;
  }

  public GuessMovieNameGameBuilder buildMovieName() {
    guessMovieNameGame.setMovieName("");
    return this;
  }

  public GuessMovieNameGame build() {
    Map<String, String> propertyMap = new HashMap<>();
    propertyMap.put("initiatorid", guessMovieNameGame.getInitiatorId());
    propertyMap.put("otherplayerid", guessMovieNameGame.getOtherPlayerId());
    propertyMap.put("gamecurrentstate", guessMovieNameGame.getGameCurrentState());
    propertyMap.put("moviename", guessMovieNameGame.getMovieName());
    DataStoreHelper.storeData(propertyMap, "gamestate");
    return guessMovieNameGame;
  }

  private String generateuniqueID(String idtype) throws DatastoreException {
    Datastore datastore = DataStoreHelper.getDatastoreConnection();
    while (true) {
      int probableUrl = Math.abs(String.valueOf(System.currentTimeMillis() * Math.random())
          .hashCode());

      GqlQuery.Builder query = GqlQuery.newBuilder().setQueryString(
          "SELECT * FROM gamestate where " + idtype + "=@id");
      query.addNameArgBuilder().setName("id").setValue(makeValue(String.valueOf(probableUrl)));
      RunQueryRequest runQueryRequest = RunQueryRequest.newBuilder().setGqlQuery(query).build();
      RunQueryResponse runQueryResponse = datastore.runQuery(runQueryRequest);
      if (runQueryResponse.getBatch().getEntityResultList().isEmpty()) {
        return String.valueOf(probableUrl);
      }
    }
  }
}
