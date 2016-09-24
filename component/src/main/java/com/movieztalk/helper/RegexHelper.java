package com.movieztalk.helper;

import java.util.regex.Pattern;

public class RegexHelper {
	
	private static RegexHelper instance;
	
	public Pattern commaPattern = Pattern.compile(",");
	public Pattern spacePattern = Pattern.compile(" ");
	
	private RegexHelper(){}
	

	public static synchronized RegexHelper getInstance(){
		if(instance==null){
			instance = new RegexHelper();
		}
		return instance;
	}
}
