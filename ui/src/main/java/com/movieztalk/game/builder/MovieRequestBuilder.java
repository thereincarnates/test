package com.movieztalk.game.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.game.model.GuessMovieNameEntity;
import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;
import com.movieztalk.game.model.ScoreBoard.PlayerScoreBoard;
import com.movieztalk.helper.MovieHelper;

import net.sf.ehcache.Element;

public class MovieRequestBuilder {

	static Logger log = Logger.getLogger(MovieRequestBuilder.class.getName());

	private final MovieRequest movieRequest;
	private final String id;
	private final String industryType;
	private final String score;
	private GuessMovieNameEntity guessMovieNameEntity;

	public MovieRequestBuilder(String id, String score, String industryType) throws DatastoreException {
		this.id = id;
		this.score = score;
		guessMovieNameEntity = fetchGameStateEntity(id);
		movieRequest = new MovieRequest();
		this.industryType = industryType;
	}

	public MovieRequestBuilder buildMovieName() throws DatastoreException {
		// Set<String> alreadyUsedMovies =
		// fetchPlayedMoviesFromEntity(gameStateEntity);
		Set<String> alreadyUsedMovies = (Set<String>) (guessMovieNameEntity.getMovieName() == null ? new HashSet<>()
				: new HashSet<>(Arrays.asList(guessMovieNameEntity.getMovieName().split(","))));
		String movie = buildNewMovieForPlayer(alreadyUsedMovies, industryType);
		movie = movie.toUpperCase().replaceAll(" ", ",");
		movieRequest.setMoviename(movie);
		return this;
	}

	public MovieRequestBuilder buildScore() throws DatastoreException {
		if (Strings.isNullOrEmpty(score)) {
			movieRequest.setScoreboard(new ScoreBoard());
			return this;
		}

		ScoreBoard sb = new ScoreBoard();
		int currentScore = 0;

		if (!Strings.isNullOrEmpty(score)) {
			currentScore = Integer.parseInt(score);
		}

		populatePlayerScoreBoard(sb.getPlayerScoreBoard1(), currentScore, guessMovieNameEntity.getScore());
		movieRequest.setScoreboard(sb);
		return this;
	}

	public MovieRequest build() {
		storeGuessMovieNameGameUpdate();
		return movieRequest;
	}

	public void storeGuessMovieNameGameUpdate() {
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			preparedStatement = connect.prepareStatement(
					"update guessmoviename set moviename = if(moviename is null, ?, concat(moviename, ?)),"
							+ "score=if(score is null, ?,concat(score, ?)) where playerid=?");
			preparedStatement.setString(1, movieRequest.getMoviename().replaceAll(",", " "));
			preparedStatement.setString(2, "," + movieRequest.getMoviename());
			preparedStatement.setString(3, score);
			preparedStatement.setString(4, "," + score);
			preparedStatement.setString(5, id);
			preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
	}

	public void populatePlayerScoreBoard(PlayerScoreBoard playerScoreBoard, int currentScore, String lastScores) {
		if (!Strings.isNullOrEmpty(lastScores)) {
			String[] scoreArr = lastScores.split(",");
			playerScoreBoard.setTotalGamePlayed(scoreArr.length);
			for (String score : scoreArr) {
				int intScore = Integer.parseInt(score);
				playerScoreBoard.setTotalScore(playerScoreBoard.getTotalScore() + intScore);
				playerScoreBoard.getScores().add(intScore);
			}
		}
		playerScoreBoard.setTotalGamePlayed(playerScoreBoard.getTotalGamePlayed() + 1);
		playerScoreBoard.setTotalScore(playerScoreBoard.getTotalScore() + currentScore);
		playerScoreBoard.getScores().add(currentScore);
	}

	/**
	 * Finds a new movie for user which he has never played before.
	 *
	 * @param id
	 *            Identifier of player
	 * @param alreadyUsedMovies
	 *            list of movies fecthed from persistent data base that are
	 *            already played by user
	 * @param industryType
	 *            industry type to fetch new movies
	 * @return new random movie which has not been played by user.
	 */
	private String buildNewMovieForPlayer(Set<String> alreadyUsedMovies, @Nonnull String industryType) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(industryType), "industry type cannot be null or empty");
		SetView<String> difference = Sets.difference(MovieHelper.getInstance().fetchMovieNamesFromDB(industryType),
				alreadyUsedMovies);
		int diffSize = difference.size();
		int random = new Random().nextInt(diffSize);
		log.info("size of the difference is" + diffSize);
		return new ArrayList<>(difference).get(random);
	}

	public GuessMovieNameEntity fetchGameStateEntity(String id) {
		GuessMovieNameEntity guessMovieNameEntity = null;
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connect.createStatement();
			String query = "select moviename, playerid, score , otherplayerid , otherplayerscore from guessmoviename where playerid='" + id +"'";
			System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				guessMovieNameEntity = new GuessMovieNameEntity();
				guessMovieNameEntity.setMovieName(resultSet.getString("moviename"));
				guessMovieNameEntity.setPlayerId(resultSet.getString("playerid"));
				guessMovieNameEntity.setScore(resultSet.getString("score"));
				guessMovieNameEntity.setOtherPlayerId(resultSet.getString("otherplayerid"));
				guessMovieNameEntity.setOtherPlayerScore(resultSet.getString("otherplayerscore"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return guessMovieNameEntity;
	}
}
