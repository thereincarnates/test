package com.movieztalk.componentclassifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

public class ComponentDictionary {

	private final String COMPONENT_RESOURCE_FILE = "/ComponentsMapping";

	private final static Map<ComponentName, Set<String>> componentDictionaryMap = new HashMap<>();

	private static ComponentDictionary instance;

	public static ComponentDictionary getInstance(){
		if(instance!=null && !componentDictionaryMap.isEmpty()){
			return instance;
		} else {
			instance = new ComponentDictionary();
			instance.fillDictionaryMap();
		}
		return instance;
	}

	public synchronized void fillDictionaryMap() {
		System.out.println(getFileWithUtil(COMPONENT_RESOURCE_FILE));
	}

	private String getFileWithUtil(String fileName) {

		String result = "";

		try {
		    result = IOUtils.toString(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	  }

	public static void main(String args[]) {
		ComponentDictionary componentDictionary = ComponentDictionary.getInstance();

	}
}
