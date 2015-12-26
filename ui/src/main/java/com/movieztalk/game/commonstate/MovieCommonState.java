package com.movieztalk.game.commonstate;

import static com.google.api.services.datastore.client.DatastoreHelper.makeFilter;
import static com.google.api.services.datastore.client.DatastoreHelper.makeProperty;
import static com.google.api.services.datastore.client.DatastoreHelper.makeValue;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Filter;
import com.google.api.services.datastore.DatastoreV1.Mutation;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.PropertyFilter;
import com.google.api.services.datastore.DatastoreV1.Query;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.movieztalk.game.model.ScoreBoard;
import com.movieztalk.helper.DataStoreHelper;

public class MovieCommonState {

  private static final Logger logger = Logger.getLogger(MovieCommonState.class.getName());

  private static MovieCommonState instance = null;
  public static final int CACHE_ELEMENT_SIZE = 1000;

  private   Multimap<String, String> playerIdToMoviesMap;
  private   Multimap<String, String> playerIdToScoreMap;

  public static MovieCommonState getInstance() {
    if(instance==null){
      logger.info("creating a new instance");
      instance = new MovieCommonState();
      instance.playerIdToMoviesMap = HashMultimap.create();
      instance.playerIdToScoreMap = ArrayListMultimap.create();
    }
    return instance;
  }

  private MovieCommonState() {
  }


  public synchronized Multimap<String, String> getPlayerIdToMovieMap(){
    return playerIdToMoviesMap;
  }

  public synchronized Multimap<String, String> getPlayerIdToScoreMap(){
    return playerIdToScoreMap;
  }

  public boolean isCacheAvailable() {
    synchronized (this) {
      if (playerIdToMoviesMap.size() < CACHE_ELEMENT_SIZE) {
        return true;
      }
      logger.info("cache is not available");
      return false;
    }
  }

  public synchronized void updateGameCommonState(String id, String movie, String score) {
    logger.info("id is " + id + "\t" + "movie is " + movie + "\t" + "score is " + score);
    playerIdToMoviesMap.put(id, movie);
    if (!Strings.isNullOrEmpty(score)) {
      if(playerIdToScoreMap.keySet().contains(id)){
        logger.info("values with key before adding" + playerIdToScoreMap.get(id));
      }
      logger.info("putting in score map" + score);
      playerIdToScoreMap.put(id, score);
      logger.info("values with key is" + playerIdToScoreMap.get(id));
    }
    flushMapsToDatastore();
  }

  public void flushMapsToDatastore() {
    if (!isCacheAvailable()) {
      logger.info("flushin the data store");
      ImmutableMultimap<String, String> playerToMoviesMapcopy;
      ImmutableMultimap<String, String> playerToScoreMapCopy;
      synchronized (this) {
        playerToMoviesMapcopy = ImmutableMultimap.copyOf(playerIdToMoviesMap);
        playerToScoreMapCopy = ImmutableMultimap.copyOf(playerIdToScoreMap);
      }
      Thread t1 = new Thread(new DataStoreFlush(playerToMoviesMapcopy, playerToScoreMapCopy));
      t1.start();
    }
  }

  /**
   * @param id
   *          identifier of player
   * @return the list of movies store in cache.
   */
  public Set<String> fetchPlayerMoviesFromCache(String id) {
    synchronized (this) {
      if (playerIdToMoviesMap.containsKey(id)) {
        return (Set<String>) playerIdToMoviesMap.get(id);
      }
      return new HashSet<>();
    }
  }

  /**
   * @param id
   *          The identifier of the player
   * @return Entity associated with the player in datastore
   * @throws DatastoreException
   */
  public static Entity fetchGameStateEntity(String id) throws DatastoreException {
    Datastore datastore = DataStoreHelper.getDatastoreConnection();
    Filter equalInitiatorIdFilter = makeFilter("initiatorid", PropertyFilter.Operator.EQUAL,
        makeValue(String.valueOf(id.toString()))).build();
    Query.Builder qBuilder = Query.newBuilder();
    qBuilder.addKindBuilder().setName("gamestate");
    qBuilder.setFilter(equalInitiatorIdFilter);
    qBuilder.setLimit(1);

    RunQueryRequest movierequest = RunQueryRequest.newBuilder().setQuery(qBuilder).build();
    RunQueryResponse movieresponse = datastore.runQuery(movierequest);

    return movieresponse.getBatch().getEntityResult(0).getEntity();
  }

}

class DataStoreFlush implements Runnable {

  private static final Logger logger = Logger.getLogger(MovieCommonState.class.getName());
  ImmutableMultimap<String, String> playerToMoviesMapcopy;
  ImmutableMultimap<String, String> playerToScoreMapCopy;

  public DataStoreFlush(ImmutableMultimap<String, String> playerToMoviesMapcopy,
      ImmutableMultimap<String, String> playerToScoreMapCopy) {
    this.playerToMoviesMapcopy = playerToMoviesMapcopy;
    this.playerToScoreMapCopy = playerToScoreMapCopy;
  }

  @SuppressWarnings("static-access")
  @Override
  public void run() {
    logger.info("running new thread");
    CommitRequest.Builder commBuilder = CommitRequest.newBuilder()
        .setMode(CommitRequest.Mode.NON_TRANSACTIONAL).setMutation(Mutation.newBuilder());
    for (String id : playerToMoviesMapcopy.keySet()) {
      try {
        Entity entity = MovieCommonState.getInstance().fetchGameStateEntity(id);
        Entity.Builder updateddata = Entity.newBuilder(entity);
        updateddata.clearProperty();
        for (Property prop : entity.getPropertyList()) {
          logger.info("inside the loop2");
          if (prop.getName().equals("moviename")) {
            Set<Value> movieList = new HashSet<>();
            movieList.addAll(prop.getValue().getListValueList());
            for (String movie : playerToMoviesMapcopy.get(id)) {
              movieList.add(makeValue(movie).build());
            }
            updateddata.addProperty(makeProperty("moviename", makeValue(movieList)));
          } else if (prop.getName().equals("scoreboard")) {

            ScoreBoard sb;
            Gson gson = new Gson();
            String scoreboard = prop.getValue().getStringValue();

            if (!scoreboard.isEmpty()) {
              sb = gson.fromJson(scoreboard, ScoreBoard.class);
            } else {
              sb = new ScoreBoard();
            }

            sb.getPlayerScoreBoard1().setTotalGamePlayed(
                sb.getPlayerScoreBoard1().getTotalGamePlayed()
                    + playerToScoreMapCopy.get(id).size());
            int totalScore = 0;
            for (String score : playerToScoreMapCopy.get(id)) {
              sb.getPlayerScoreBoard1().getScores().add(Integer.parseInt(score));
              totalScore += Integer.parseInt(score);
            }
            sb.getPlayerScoreBoard1().setTotalScore(
                sb.getPlayerScoreBoard1().getTotalScore() + totalScore);
            scoreboard = gson.toJson(sb);
            updateddata.addProperty(makeProperty("scoreboard", makeValue(scoreboard)));
          } else {
            updateddata.addProperty(prop);
          }
        }
        commBuilder.getMutationBuilder().addUpdate(updateddata.build());
      } catch (DatastoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    try {
      DataStoreHelper.getDatastoreConnection().commit(commBuilder.build());
      logger.info("done commiting");
    } catch (DatastoreException e) {
      logger.severe(e.getMessage());
      e.printStackTrace();
    }
    synchronized (MovieCommonState.getInstance()) {
      for (String key : MovieCommonState.getInstance().getPlayerIdToMovieMap().keySet()) {
        if (playerToMoviesMapcopy.containsKey(key)) {
          MovieCommonState.getInstance().getPlayerIdToMovieMap().get(key).removeAll(playerToMoviesMapcopy.get(key));
        }
      }

      for (String key : MovieCommonState.getInstance().getPlayerIdToScoreMap().keySet()) {
        if (playerToScoreMapCopy.containsKey(key)) {
          MovieCommonState.getInstance().getPlayerIdToScoreMap().get(key).removeAll(playerToScoreMapCopy.get(key));
        }
      }
    }
  }
}
