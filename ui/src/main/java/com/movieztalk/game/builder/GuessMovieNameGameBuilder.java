package com.movieztalk.game.builder;

import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.util.UniqueIdGenerator;
import com.google.api.services.datastore.client.DatastoreException;

public class GuessMovieNameGameBuilder {

	private UniqueIdGenerator uniqueIdGenerator = UniqueIdGenerator.getInstance();
	private final GuessMovieNameGame guessMovieNameGame;

	public GuessMovieNameGameBuilder() {
		guessMovieNameGame = new GuessMovieNameGame();
	}

	public GuessMovieNameGame build() {
		return guessMovieNameGame;
	}

	public GuessMovieNameGameBuilder buildGameState() {
		guessMovieNameGame.setGameCurrentState("");
		return this;
	}

	public GuessMovieNameGameBuilder buildInitiatorId() throws DatastoreException {
		String initiatorId = uniqueIdGenerator.generateUniqueId();
		guessMovieNameGame.setInitiatorId(initiatorId);
		return this;
	}

	public GuessMovieNameGameBuilder buildMovieName() {
		guessMovieNameGame.setMovieName("");
		return this;
	}

	public GuessMovieNameGameBuilder buildOtherPlayerId() throws DatastoreException {
		String otherPlayerId = uniqueIdGenerator.generateUniqueId();
		guessMovieNameGame.setOtherPlayerId(otherPlayerId);
		return this;
	}
}
