package com.movieztalk.demo;

import java.util.UUID;

public class UUIDDemo {


	public static void main(String args[]) {
		for(int i=0;i<100;i++) {
			System.out.println(UUID.randomUUID().toString());
		}
	}
}
