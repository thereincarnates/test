package com.movieztalk.UUID;

import java.util.UUID;

public class UUIDGenerator {
	
	private static UUIDGenerator instance;
	private UUIDGenerator(){}
	
	public static synchronized UUIDGenerator getInstance(){
		if(instance == null){
			instance = new UUIDGenerator();
		}
		return instance;
	}
	
	public synchronized String fetchUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] args){
		System.out.println(new UUIDGenerator().fetchUUID());
	}
}
