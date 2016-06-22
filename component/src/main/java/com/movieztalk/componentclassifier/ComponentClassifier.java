package com.movieztalk.componentclassifier;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComponentClassifier {
	
	public HashSet<String> classify(ComponentDictionary compDict, String[] tweetToken)
	{
		System.out.println("Classification Started:");
		HashSet<String> componentClass = new HashSet<String>();
		for (String token: tweetToken)
		{

			Iterator entries =  compDict.getComponentDictionary().entrySet().iterator();
			
			while(entries.hasNext())
			{
				Map.Entry entry = (Map.Entry) entries.next();
			    Set<String> wordList = (Set<String>) entry.getValue();
			    
			    boolean isPresent = wordList.contains(token);
			    if(isPresent == true)
			    {
			    	
			    	componentClass.add((String)entry.getKey());
			    }
			}
		}
		return componentClass;
	}
   
}