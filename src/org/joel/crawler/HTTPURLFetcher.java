package org.joel.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class HTTPURLFetcher implements URLFetcher {
	
	/**
	 * Fetches the content of a URL from the network
	 *@param String url
	 *				the url to fetch
	 *@return the list of links read from the HTML
	 */
	@Override
	public List<String> fetch(String url) {
		try {
			InputStream in = new URL(url).openStream();
			return HTMLRead.getURLs(new InputStreamReader(in), url);
		} catch (IOException e) {
			return null;
		}
	}
}