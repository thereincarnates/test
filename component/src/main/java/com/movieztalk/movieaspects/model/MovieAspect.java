package com.movieztalk.movieaspects.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents those entities of movie which has reviews and ratings.
 */
public class MovieAspect {

	// Rating of the movie aspect
	private double rating;
	// Positive comments about the aspect
	private final Set<String> positiveReviews = new LinkedHashSet<>();
	// Negative comments about the aspect
	private final Set<String> negativeReviews = new LinkedHashSet<>();
	
	public double getRating() {
		return rating;
	}

	public MovieAspect setRating(double rating) {
		this.rating = rating;
		return this;
	}

	public Set<String> getPositiveReviews() {
		return positiveReviews;
	}

	public Set<String> getNegativeReviews() {
		return negativeReviews;
	}
}
