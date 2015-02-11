package org.joel.crawler;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.List;

/**
 * A crawler 
 * 
 *
 */
public class WebCrawler {

	private String url;
	private int maxDepth;
	private Writer output;
	Queue<Candidate> candidates;
	Set<String> visited;
	URLFetcher fetcher;
	
	public WebCrawler(String url, int maxDepth, Writer output,
			Queue<Candidate> candidates, Set<String> visited, URLFetcher fetcher) {
		this.url = url;
		this.maxDepth = maxDepth;
		this.output = output;
		this.candidates = candidates;
		this.visited = visited;
		this.fetcher = fetcher;
	}
	
	/**
	 * Create a crawler
	 * @param url
	 * 			the url to start with
	 * @param maxDepth
	 * 			an int indicating the level of depth to finish
	 * @param output
	 * 			a Writer to output the list of urls found
	 */
	public WebCrawler(String url, int maxDepth, Writer output) {
		this(url, maxDepth, output, new LinkedList<Candidate>(),
				new HashSet<String>(), new HTTPURLFetcher());
	}
	
	/**
	 * Crawls a network of Hypertext links and outputs all URL links
	 * visited
	 * @throws IOException 
	 */
	public void crawl() throws IOException {
		candidates.add(new Candidate(0, this.url));
		visited.add(this.url);
		while (!candidates.isEmpty()) {
			Candidate candidate = candidates.poll();
			if (search(candidate.url)) {
				output.write(candidate.url + "\n");
			}
			if (candidate.level < maxDepth) {
				List<String> urls = fetcher.fetch(candidate.url);
				if (urls != null) {
					for (String url : urls) {
						if (!visited.contains(url)) {
							visited.add(url);
							candidates.add(new Candidate(candidate.level + 1,
									url));
						}
					}
				}
			}
		}

	}
	
	/**
	 * Decide whether the url needs to be recorded or not.
	 * @param url
	 * @return true if the url matches the given criteria 
	 * (when a future programmer overrides the method)
	 */
	public boolean search(String url) {
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
