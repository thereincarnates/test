package com.movieztalk.util;

import java.util.UUID;

public class UniqueIdGenerator {

	private static UniqueIdGenerator instance = new UniqueIdGenerator();

	public static UniqueIdGenerator getInstance() {
		return instance;
	}

	public synchronized String generateUniqueId() {
		return UUID.randomUUID().toString();
	}
}
