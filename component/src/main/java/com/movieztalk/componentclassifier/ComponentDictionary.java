package com.movieztalk.componentclassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

/**
 * Represent details of component and there associated keywords
 */
public final class ComponentDictionary {

	private static ComponentDictionary instance;

	// contains mapping of keywords to the component
	private Map<String, Set<String>> componentDictionary;

	private ComponentDictionary() {
	}

	public static synchronized ComponentDictionary getInstance() {
		if (instance == null) {
			Yaml yaml = new Yaml();
			try (InputStream in = ComponentDictionary.class.getResourceAsStream("/component.yml")) {
				instance = yaml.loadAs(in, ComponentDictionary.class);
			} catch (Exception e) {
				System.out.println("received following error" + e.getMessage());
			}
		}
		return instance;
	}

	public Map<String, Set<String>> getComponentDictionary() {
		return componentDictionary;
	}

	public void setComponentDictionary(Map<String, Set<String>> componentDictionary) {
		this.componentDictionary = componentDictionary;
	}
}
