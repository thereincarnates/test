package com.movieztalk.demo;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhCacheDemo {
	
	public static void main(String args[]){
		CacheManager cm = CacheManager.getInstance();
		Cache cache = cm.getCache("gameManager");
		cache.put(new Element("1","Jan"));
		cache.put(new Element("2","Feb"));
		cache.put(new Element("3","Mar"));
		Element ele = cache.get("8");

		//5. Print out the element
		String output = (ele == null ? null : ele.getObjectValue().toString());
		System.out.println(output);
	}

}
