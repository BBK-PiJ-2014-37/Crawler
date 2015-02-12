package org.joel.crawler;

public class Candidate {
	
	public final int level;
	public final String url;
	
	public Candidate(int level, String url) {
		this.level = level;
		this.url = url;
	}
	
	public boolean equals(Candidate c) {
		return (this.level == c.level && this.url.equals(c.url));
	}

}
