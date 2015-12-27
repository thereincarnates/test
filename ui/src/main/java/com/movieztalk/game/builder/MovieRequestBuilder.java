package com.movieztalk.game.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.gson.Gson;
import com.movieztalk.game.commonstate.MovieCommonState;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;
import com.movieztalk.util.MovieGameUtil;

public class MovieRequestBuilder {

  static Logger log = Logger.getLogger(MovieRequestBuilder.class.getName());

  private final MovieRequest movieRequest;
  private final Entity gameStateEntity;
  private final String id;
  private final String industryType;
  private final String score;

  public MovieRequestBuilder(String id, String score, String industryType)
      throws DatastoreException {
    this.id = id;
    this.score = score;
    gameStateEntity = MovieCommonState.fetchGameStateEntity(id);
    log.info("game state entity is " + gameStateEntity.toString());
    movieRequest = new MovieRequest();
    this.industryType = industryType;
  }

  public MovieRequestBuilder buildMovieName() throws DatastoreException {
    Set<String> alreadyUsedMovies = fetchPlayedMoviesFromEntity(gameStateEntity);
    String movie = buildNewMovieForPlayer(id, alreadyUsedMovies, industryType);
    log.info("movie name found is" + movie);
    movieRequest.setMoviename(movie);
    return this;
  }

  public MovieRequestBuilder buildScore() throws DatastoreException {
    if (Strings.isNullOrEmpty(score)) {
      movieRequest.setScoreboard(new ScoreBoard());
      return this;
    }

    String scoreboard = "";
    Gson gson = new Gson();
    ScoreBoard sb = new ScoreBoard();
    int score1 = 0;

    if (!Strings.isNullOrEmpty(score)) {
      score1 = Integer.parseInt(score);
    }

    for (Property prop : gameStateEntity.getPropertyList()) {
      if (prop.getName().equals("scoreboard")) {
        scoreboard = prop.getValue().getStringValue();
        if (!Strings.isNullOrEmpty(scoreboard)) {
          sb = gson.fromJson(scoreboard, ScoreBoard.class);
        } else {
          sb = new ScoreBoard();
        }
        /*sb.getPlayerScoreBoard1().setTotalGamePlayed(
            sb.getPlayerScoreBoard1().getTotalGamePlayed() + 1);
        sb.getPlayerScoreBoard1().setTotalScore(sb.getPlayerScoreBoard1().getTotalScore() + score1);
        sb.getPlayerScoreBoard1().getScores().add(score1);*/
      }
    }

    if (MovieCommonState.getInstance().getPlayerIdToScoreMap().containsKey(id)) {
      for (String scores : MovieCommonState.getInstance().getPlayerIdToScoreMap().get(id)) {
        log.info("score received from ram is" + scores);
        if (!Strings.isNullOrEmpty(scores)) {
          int score11 = Integer.parseInt(scores);
          sb.getPlayerScoreBoard1().setTotalGamePlayed(
              sb.getPlayerScoreBoard1().getTotalGamePlayed() + 1);
          sb.getPlayerScoreBoard1().setTotalScore(
              sb.getPlayerScoreBoard1().getTotalScore() + score11);
          sb.getPlayerScoreBoard1().getScores().add(score11);
        }
      }
    }
    sb.getPlayerScoreBoard1().setTotalGamePlayed(
        sb.getPlayerScoreBoard1().getTotalGamePlayed() + 1);
    sb.getPlayerScoreBoard1().setTotalScore(sb.getPlayerScoreBoard1().getTotalScore() + score1);
    sb.getPlayerScoreBoard1().getScores().add(score1);
    log.info("score board returned is " + sb.toString());
    movieRequest.setScoreboard(sb);
    return this;
  }

  public MovieRequest build() {
    MovieCommonState.getInstance().updateGameCommonState(id, movieRequest.getMoviename(), score);
    return movieRequest;
  }

  /**
   * @param gameStateEntity
   *          Player gamestate entity
   * @return list of movies already used by player
   */
  private Set<String> fetchPlayedMoviesFromEntity(Entity gameStateEntity) {
    Set<String> alreadyUsedMovies = new HashSet<>();
    for (Property property : gameStateEntity.getPropertyList()) {
      if (property.getName().equalsIgnoreCase("moviename")) {
        for (Value value : property.getValue().getListValueList()) {
          alreadyUsedMovies.add(value.getStringValue());
        }
      }
    }
    return alreadyUsedMovies;
  }

  /**
   * Finds a new movie for user which he has never played before.
   *
   * @param id
   *          Identifier of player
   * @param alreadyUsedMovies
   *          list of movies fecthed from persistent data base that are already played by user
   * @param industryType
   *          industry type to fetch new movies
   * @return new random movie which has not been played by user.
   */
  private String buildNewMovieForPlayer(String id, Set<String> alreadyUsedMovies,
      String industryType) {
    alreadyUsedMovies.addAll(MovieCommonState.getInstance().fetchPlayerMoviesFromCache(id));
    SetView<String> difference = Sets.difference(MovieGameUtil.fetchMovieNames(industryType),
        alreadyUsedMovies);
    // TODO make new movie generation intelligent with player skills and score.
    int diffSize = difference.size();
    int random = new Random().nextInt(diffSize);
    log.info("size of the difference is" + diffSize);
    return new ArrayList<>(difference).get(random);
  }
}
