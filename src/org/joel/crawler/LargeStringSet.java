package org.joel.crawler;

import java.util.HashSet;

public class LargeStringSet implements StringSet {
	private HashSet<String> visited;

	public LargeStringSet() {
		this.visited = new HashSet<String>();
	}
	
	public boolean contains(String url) {
		return visited.contains(url);
	}

	public void add(String url) {
		visited.add(url);		
	}

}
