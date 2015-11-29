package com.movieztalk.game.builder;

import static com.google.api.services.datastore.client.DatastoreHelper.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.Other;

import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.helper.DataStoreHelper;
import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.DatastoreV1.Filter;
import com.google.api.services.datastore.DatastoreV1.GqlQuery;
import com.google.api.services.datastore.DatastoreV1.PropertyFilter;
import com.google.api.services.datastore.DatastoreV1.Query;
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
    String initiatorId = generateuniqueID();
    guessMovieNameGame.setInitiatorId(initiatorId);
    return this;
  }

  public GuessMovieNameGameBuilder buildOtherPlayerId() throws DatastoreException {
    String otherPlayerId = generateuniqueID();
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
    propertyMap.put("scoreboard","");
    DataStoreHelper.storeData(propertyMap, "gamestate");
    return guessMovieNameGame;
  }

  private String generateuniqueID() throws DatastoreException {
	  
	  Datastore  datastore =  DataStoreHelper.getDatastoreConnection();
      
	  while (true) {
      int probableUrl = Math.abs(String.valueOf(System.currentTimeMillis() * Math.random())
          .hashCode());
      
      System.out.println("fetching first entry from ds" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
      Filter equalInitiatorIdFilter = makeFilter(
    		    "initiatorid", PropertyFilter.Operator.EQUAL, makeValue(String.valueOf(probableUrl))).build();
      
      Query.Builder q = Query.newBuilder();
      q.addKindBuilder().setName("gamestate");
      q.setFilter(equalInitiatorIdFilter)
      .setLimit(1).build();
      
      RunQueryRequest request = RunQueryRequest.newBuilder().setQuery(q).build();
      RunQueryResponse response = datastore.runQuery(request);
      System.out.println("done fetching first entry from ds" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
      
      Filter equalOtherPlayerIdFilter = makeFilter(
  		    "otherplayerid", PropertyFilter.Operator.EQUAL, makeValue(String.valueOf(probableUrl))).build();

     
      Query.Builder q1 = Query.newBuilder();
      q1.addKindBuilder().setName("gamestate");
      q1.setFilter(equalOtherPlayerIdFilter)
      .setLimit(1).build();
      
      
      RunQueryRequest request1 = RunQueryRequest.newBuilder().setQuery(q1).build();
      RunQueryResponse response1 = datastore.runQuery(request1);
      
      System.out.println("done fetching second entry from ds" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
      
      System.out.println("movie" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
      

      Filter movieFilter = makeFilter(
    		    "name", PropertyFilter.Operator.EQUAL, makeValue("prem")).build();

      Query.Builder q2 = Query.newBuilder();
      q2.addKindBuilder().setName("movie");
      q1.setFilter(movieFilter)
      .setLimit(1).build();
      
      
      RunQueryRequest request2 = RunQueryRequest.newBuilder().setQuery(q2).build();
      RunQueryResponse response2 = datastore.runQuery(request2);
      System.out.println("movie" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
      



       if ((response.getBatch().getEntityResultList()).isEmpty() &&
    		  ((response1.getBatch().getEntityResultList()).isEmpty())) {
        return String.valueOf(probableUrl);
      }
    }
  }
}
