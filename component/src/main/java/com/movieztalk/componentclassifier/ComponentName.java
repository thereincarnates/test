package com.movieztalk.componentclassifier;

import static java.lang.String.format;

public final class ComponentName{
	private String componentName;
	
	public ComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	private String getComponentName() {
		return componentName;
	}
	
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	 
	@Override
	public String toString() {
	        return new StringBuilder()
	            .append( format(componentName ) )
	            .toString();
	 }
	
}

