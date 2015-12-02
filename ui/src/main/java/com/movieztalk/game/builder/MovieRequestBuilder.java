package com.movieztalk.game.builder;

import static com.google.api.services.datastore.client.DatastoreHelper.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.DatastoreV1.Filter;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.PropertyFilter;
import com.google.api.services.datastore.DatastoreV1.Query;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.gson.Gson;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;
import com.movieztalk.helper.DataStoreHelper;
import com.movieztalk.util.MovieGameUtil;

public class MovieRequestBuilder {

  static Logger log = Logger.getLogger(MovieRequestBuilder.class.getName());

  private final MovieRequest movieRequest;

  public MovieRequestBuilder() {
    movieRequest = new MovieRequest();
  }

  public MovieRequestBuilder buildMovieRequest(String id, String score) {
    try {
      movieRequest.setMoviename(fetchMovieName(id, score).toUpperCase().replaceAll(" ", " , "));
    } catch (DatastoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this;
  }

  public MovieRequestBuilder buildScore() {
    ScoreBoard scorebd = new ScoreBoard();
    movieRequest.setScoreboard(scorebd);

    return this;
  }

  public MovieRequest build() {
    Map<String, String> propertyMap = new HashMap<>();
    return movieRequest;
  }


  private String fetchMovieName(String id, String score) throws DatastoreException {
    Set<String> alreadyUsedMovies = new HashSet<>();
    Entity gameStateEntity = fetchGameStateEntity(id, alreadyUsedMovies);
    String movie = buildNewMovieForPlayer(alreadyUsedMovies);
    updateGameState(movie, gameStateEntity, score);
    log.info("Movie name is" + movie);
    return movie;
  }

  private void updateGameState(String movie, Entity gameStateEntity, String score) {

    Entity.Builder updateddata = Entity.newBuilder(gameStateEntity);
    updateddata.clearProperty();
    for (Property prop : gameStateEntity.getPropertyList()) {
      System.out.println("inside the loop2");
      if (prop.getName().equals("moviename")) {

        Set<Value> movieList = new HashSet<>();
        movieList.addAll(prop.getValue().getListValueList());
        movieList.add(makeValue(movie).build());
        updateddata.addProperty(makeProperty("moviename", makeValue(movieList)));
      } else if (prop.getName().equals("scoreboard")) {
        int score1 = 0;
        if (!Strings.isNullOrEmpty(score)) {
          score1 = Integer.parseInt(score);
        }
        ScoreBoard sb;
        Gson gson = new Gson();
        String scoreboard = prop.getValue().getStringValue();

        if (!scoreboard.isEmpty()) {
          sb = gson.fromJson(scoreboard, ScoreBoard.class);
          sb.getPlayerScoreBoard1().setTotalGamePlayed(
              sb.getPlayerScoreBoard1().getTotalGamePlayed() + 1);
          sb.getPlayerScoreBoard1().setTotalScore(
              sb.getPlayerScoreBoard1().getTotalScore() + score1);
          sb.getPlayerScoreBoard1().getScores().add(score1);
        } else {
          sb = new ScoreBoard();
        }

        scoreboard = gson.toJson(sb);
        movieRequest.setScoreboard(sb);
        updateddata.addProperty(makeProperty("scoreboard", makeValue(scoreboard)));
      } else {
        updateddata.addProperty(prop);
      }
    }
    DataStoreHelper.UpdateEntity(updateddata);
  }

  private String buildNewMovieForPlayer(Set<String> alreadyUsedMovies) {
     SetView<String>  difference = Sets.difference(MovieGameUtil.fetchMovieNames("bollywood"),alreadyUsedMovies);
     int random = new Random().nextInt(difference.size());
     return new ArrayList<>(difference).get(random);
  }

  private Entity fetchGameStateEntity(String id, Set<String> alreadyUsedMovies) throws DatastoreException {
    Datastore datastore = DataStoreHelper.getDatastoreConnection();
    Filter equalInitiatorIdFilter = makeFilter("initiatorid", PropertyFilter.Operator.EQUAL,
        makeValue(String.valueOf(id.toString()))).build();
    Query.Builder qBuilder = Query.newBuilder();
    qBuilder.addKindBuilder().setName("gamestate");
    qBuilder.setFilter(equalInitiatorIdFilter);
    qBuilder.setLimit(1);

    RunQueryRequest movierequest = RunQueryRequest.newBuilder().setQuery(qBuilder).build();
    RunQueryResponse movieresponse = datastore.runQuery(movierequest);

    Entity gameStateEntity = movieresponse.getBatch().getEntityResult(0).getEntity();
    for(Property property :  gameStateEntity.getPropertyList()){
      if(property.getName().equalsIgnoreCase("moviename")){
        for(Value value : property.getValue().getListValueList()){
          alreadyUsedMovies.add(value.getStringValue());
        }
      }
    }
    return gameStateEntity;
  }
}
