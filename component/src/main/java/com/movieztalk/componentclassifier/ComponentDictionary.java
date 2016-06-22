package com.movieztalk.componentclassifier;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class contains details of component and there associated keywords
 * @author ragrawal
 *
 */
public final class ComponentDictionary {
	
	/**
	 * numberofComp specifies number of class used i.e Story, Direction etc
	 */
	private Integer numberOfComp;
	
	/**
	 * List contains the name of the different components
	 */
	private List<ComponentName> componentList;
	
	/**
	 * Contains mapping of keywords to the component  
	 */
	private Map<String,Set<String>> componentDictionary;
	
	/**
	 * StopWords are words which are not important for classification
	 */
	private List<String> stopWords;
	
	public List<String> getStopWords() {
		return stopWords;
	}

	public void setStopWords(List<String> stopWords) {
		this.stopWords = stopWords;
	}

	public Integer getNumberOfComp() {
		return numberOfComp;
	}
	
	public void setNumberOfComp(Integer numberOfComp) {
		this.numberOfComp = numberOfComp;
	}
	
	public List<ComponentName> getComponentList() {
		return componentList;
	}
	
	public void setComponentList(List<ComponentName> componentList) {
		this.componentList = componentList;
	}
	
	public Map<String, Set<String>> getComponentDictionary() {
		return componentDictionary;
	}
	
	public void setComponentDictionary(Map<String, Set<String>> componentDictionary) {
		this.componentDictionary = componentDictionary;
	}
	
	@Override
	public String toString() {
	        return new StringBuilder()
	            .append( format( "numberOfComp: %s\n", numberOfComp ) )
	            .append( format( "ListofComponent: %s\n", componentList ) )
	            .append( format( "componentDictionary: %s\n", componentDictionary) )
	            .append( format( "stopWords:%s\n", stopWords) )
	            .toString();
	}
}
