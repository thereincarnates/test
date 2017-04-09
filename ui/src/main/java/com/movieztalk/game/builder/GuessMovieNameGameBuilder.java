package com.movieztalk.game.builder;

import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.util.UniqueIdGenerator;

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

	public GuessMovieNameGameBuilder buildInitiatorId() {
		String initiatorId = uniqueIdGenerator.generateUniqueId();
		guessMovieNameGame.setInitiatorId(initiatorId);
		return this;
	}

	public GuessMovieNameGameBuilder buildMovieName() {
		guessMovieNameGame.setMovieName("");
		return this;
	}

	public GuessMovieNameGameBuilder buildOtherPlayerId() {
		String otherPlayerId = uniqueIdGenerator.generateUniqueId();
		guessMovieNameGame.setOtherPlayerId(otherPlayerId);
		return this;
	}
}
