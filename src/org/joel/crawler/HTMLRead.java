package org.joel.crawler;

import java.io.Reader;

public abstract class HTMLRead {

	/**
	 * Returns an array with all the full URLs for links contained in
	 * the source, using the provided url to compute the default base.
	 * 
	 * @param source a stream containing a HTML document
	 * @param url the URL from which the stream originates
	 * @return array of Strings containing all URLs for this document
	 */
	public static String[] getURLs(Reader source, String url) {
		return new String[0];
	}

}
