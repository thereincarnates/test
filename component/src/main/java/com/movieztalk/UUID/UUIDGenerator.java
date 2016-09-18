package com.movieztalk.UUID;

import java.util.UUID;

public class UUIDGenerator {
	
	public synchronized String fetchUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] args){
		System.out.println(new UUIDGenerator().fetchUUID());
	}
}
