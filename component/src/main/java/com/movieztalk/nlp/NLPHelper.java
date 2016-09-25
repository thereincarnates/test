package com.movieztalk.nlp;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

public class NLPHelper {

	private static NLPHelper instance;
	
	public static synchronized NLPHelper getInstance(){
		if(instance==null){
			instance = new NLPHelper();
		}
		return instance;
	}
	
	private NLPHelper(){}
	
	
	public Set<String> convertKeyWord(String keyWord){
		Preconditions.checkNotNull(keyWord);
		Set <String> keyWords = new HashSet<>();
		if(keyWord.trim().startsWith("{")){
			keyWords.add(keyWord.replaceAll("\\{|\\}", ""));
		}
		return keyWords;
	}
	
	public static void main(String [] args){
		NLPHelper helper = new NLPHelper();
		System.out.println(helper.convertKeyWord("{kuch bhi}"));
	}
}
