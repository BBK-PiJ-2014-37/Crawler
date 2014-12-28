package org.joel.crawler;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class HTMLRead {

	/**
	 * Returns an array with all the full URLs for links contained in
	 * the source, using the provided url to compute the default base.
	 * 
	 * @param source a stream containing a HTML document
	 * @param url the URL from which the stream originates
	 * @return array of Strings containing all URLs for this document
	 */
	public static List<String> getURLs(Reader source, String url) throws IOException{
		// compute base
		List<String> urls = new ArrayList<String>();
		while (true) {
			String tag = getNextTag(source);
			if (tag.equals("")) {
				return urls;
			}
			// If tag is not either "a" or "base", continue
			// get contents of "href" attribute
			// if there was no href attribute, continue
			// if tag is base:
			//     if there was no previous base tag, change the base
			// else 
			//     build the URL from base and href
			//     add URL to list
		}
	}
		
	/**
	 * Get the contents of the next HTML tag.
	 * 
	 * Finds the next tag in the input stream, and returns its contents in a
	 * String. The string contains all the text between the "<" and ">"
	 * characters. Whitespace in the string are converted into spaces. A series
	 * of consecutive whitespace characters are converted to as single one.
	 * 
	 * @param source the input stream
	 * @return the contents of the next tag
	 * @throws IOException
	 */
	private static String getNextTag(Reader source) throws IOException {
		String tag = "";
		int c = 0;
		Character ch = ' '; 
		do {
			c = source.read();
			ch = (char)c;
		}  while (c != -1 && !ch.equals("<"));
		while (true) {
			c = source.read();
			ch = (char)c;
			if (c == -1 || ch.equals(">")) {
				return tag;
			}
			if (Character.isWhitespace(ch)) {
				while(c != -1 && Character.isWhitespace(ch)) {
					c = source.read();
					ch = (char)c;
				}
				tag += " ";
				if (c == -1 || ch.equals(">")) {
					return tag;
				}
			}
			tag += ch;
		}
	}
}