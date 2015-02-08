package org.joel.crawler;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class HTMLRead {

	/**
	 * Returns an array with all the full URLs for links contained in the
	 * source, using the provided url to compute the default base.
	 * 
	 * @param source
	 *            a stream containing a HTML document
	 * @param url
	 *            the URL from which the stream originates
	 * @return array of Strings containing all URLs for this document
	 */
	public static List<String> getURLs(Reader source, String url)
			throws IOException {
		PushbackReader pbSource = new PushbackReader(source);
		String base = computeBase(url);
		boolean baseTagSeen = false;
		List<String> urls = new ArrayList<String>();
		String newUrl = "";
		while (true) {
			String tag = getNextTag(pbSource);
			if (tag.equals("")) {
				return urls;
			}
			ArrayList<String> elements = getElements(tag);
			if(elements.size() == 0) {
				continue;
			}
			if (elements.get(0).equals("a")) {
				newUrl = getHrefValue(elements);
				if (newUrl != null) {
					if (!newUrl.startsWith("http:/") && !newUrl.startsWith("/")) {
						newUrl = base + newUrl;					
					}
					urls.add(newUrl);
				}				
			} else if (!baseTagSeen && elements.get(0).equals("base")) {
				newUrl= getHrefValue(elements);
				if (newUrl != null) {
					if (newUrl.charAt(newUrl.length()-1) == '/') {
						base = newUrl;
					} else {
						base = newUrl + "/";
					}
					baseTagSeen = true;
				}
			}
		}
	}
	
	/**
	 * Split the tag's contents into elements. Each element is a sequence of
	 * non-whitespace characters. If either single or double quotes are encountered,
	 * the element ends at the matching quote, and whitespace is preserved.
	 * 
	 * Example: "foo bar foobar'bar foo'baz" yields ["foo", "bar", "foobar'bar foo'", "baz"]
	 * 
	 * @param tag
	 * 			a String with the contents of the tag.
	 * @return
	 * 			the list of elements.
	 */
	private static ArrayList<String> getElements(String tag) {
		int len = tag.length();
		int i = 0;
		ArrayList<String> result = new ArrayList<String>();
		while (i < len) {
			while (i < len && Character.isWhitespace(tag.charAt(i))) {
				i++;
			}
			int start = i;
			char quoteChar = ' ';
			while (i < len && (!Character.isWhitespace(tag.charAt(i)) || quoteChar != ' ')) {
				char c = tag.charAt(i);
				i++;
				if (quoteChar != ' ' && c == quoteChar) {
					break;
				}
				if (c == '"' || c == '\'') {
					if (quoteChar == ' ') {
						quoteChar = c;
					}
				}
			}
			result.add(tag.substring(start, i));
		}
		return result;
	}
	
	/**
	 * If the list contains an element that starts with "href=", returns the 
	 * value of the attribute. Otherwise, returns null.
	 * 
	 * @param elements
	 * 			a list of strings representing the contents of a tag
	 * @return the value of the "href" attribute, or null if none
	 */
	private static String getHrefValue(List<String> elements) {
		final String attrPrefix = "href=";
		int len = elements.size();
		String result = null;
		for(int i = 1; i < len; i++) {
			if(elements.get(i).startsWith(attrPrefix)) {
				result = elements.get(i).substring(attrPrefix.length());
				char c = result.charAt(0);
				if(c == '"' || c == '\'' ) {
					if (c == result.charAt(result.length() - 1)) {
						return result.substring(1,result.length() - 1);
					} else {
						return result.substring(1);
					}
				}
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the base of the given url
	 * 
	 * @param url
	 *            the url from which to extract the base
	 * @return the base
	 */
	private static String computeBase(String url) {
		int end = url.lastIndexOf('/');
		return end < 0 ? url : url.substring(0, end+1);
	}

	/**
	 * Get the contents of the next HTML tag.
	 * 
	 * Finds the next tag in the input stream, and returns its contents in a
	 * String. The string contains all the text between the "<" and ">"
	 * characters. Whitespace in the string are converted into spaces. A series
	 * of consecutive whitespace characters are converted to as single one.
	 * 
	 * Example: consecutive calls to this method on a Reader containing
	 * "blah <foo> abc < bar baz > </bar> <biz boz buz>" return "foo",
	 * "bar baz", "/bar" "biz boz buz"
	 * 
	 * @param source
	 *            the input stream
	 * @return the contents of the next tag
	 * @throws IOException
	 */
	private static String getNextTag(PushbackReader source) throws IOException {
		String tag = "";
		int c = 0;
		char ch = ' ';
		do {
			c = source.read();
			ch = (char)c;
		} while (c != -1 && ch != '<');
		while (true) {
			c = source.read();
			ch = (char) c;
			if (c == -1 || ch == '>') {
				return tag;
			}
			if (Character.isWhitespace(ch)) {
				while (c != -1 && Character.isWhitespace(ch)) {
					c = source.read();
					ch = (char) c;
				}
				tag += " ";
				if (c == -1 || ch == '>' || ch == '<') {
					if (ch == '<') {
						source.unread(ch);
					}
					return tag;
				}
			}
			tag += ch;
		}
	}
}