package org.joel.crawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;


class URLFetcherMock implements URLFetcher {

	// Pretend World-Wide-Web
	private static final Hashtable<String, String[]> WWW =
 new Hashtable<String, String[]>() {
		{
			put("http://foo.com/",
					new String[] { "http://foo.com/contents.html" });
			put("http://bbc4.com/", new String[] { "http://bbc4.com/menu.html",
					"http://foo.com/", "http://bbc4.com/whatson/" });
			put("http://bbc.com/", new String[] { "http://bbc4.com/",
					"http://bbc3.com/", "http://bbc1.com/", "http://foo.com/" });
			put("http://www.radiolive.com/", new String[] { "http://bbc.com/",
					"http://heart.com", "http://foo.com/" });
			put("http://bbc4.com/whatson/", new String[] {
					"http://bbc4.com/whatson/thisweek/",
					"http://bbc4.com/whatson/nextweek/" });

	}};
	
	public List<String> fetch(String url) {
		String[] urls = WWW.get(url);
		if (urls == null) {
			return null;
		}
		return Arrays.asList(urls);
	}
}

/**
 * A class to test the search method matching an specific criteria
 * 
 *
 */
class SearchTestWebCrawler extends WebCrawler {
	
	public SearchTestWebCrawler(String url, int maxDepth, Writer output,
			Queue<Candidate> candidates, Set<String> visited, URLFetcher fetcher) {
		super(url, maxDepth, output, candidates, visited, fetcher);
	}
/**
 * Override search method to return false 
 * when the url does not finish with "/"
 */

	@Override
	public boolean search(String url) {
		return (url.endsWith("/"));
	}
}
/**
 * A class to setup the conditions for the test
 * 
 *
 */
public class WebCrawlerTest {
	
	private static String doTest(String url, int maxDepth) throws IOException {
		StringWriter output = new StringWriter();
		WebCrawler crawler = new WebCrawler(url, maxDepth, output,
				new LinkedList<Candidate>(), new HashSet<String>(),
				new URLFetcherMock());
		crawler.crawl();
		return output.toString();
	}

	@Test
	public void testCrawlZeroDepth() throws IOException {
		assertEquals("Depth = 0",
				"http://www.radiolive.com/\n",
				doTest("http://www.radiolive.com/", 0));
	}
	
	@Test
	public void testCrawlOneDepth() throws IOException {
		assertEquals(
				"Depth = 1",
				"http://www.radiolive.com/\nhttp://bbc.com/\nhttp://heart.com\nhttp://foo.com/\n",
				doTest("http://www.radiolive.com/", 1));

	}
	@Test
	public void testCrawlTwoDepth() throws IOException {
		assertEquals(
				"Depth = 2",
				"http://www.radiolive.com/\nhttp://bbc.com/\nhttp://heart.com\nhttp://foo.com/\n"
				+ "http://bbc4.com/\nhttp://bbc3.com/\nhttp://bbc1.com/\nhttp://foo.com/contents.html\n",
				doTest("http://www.radiolive.com/", 2));
	}
	
	@Test
	public void testCrawlFourDepth() throws IOException {
		assertEquals(
				"Depth = 4",
				"http://www.radiolive.com/\nhttp://bbc.com/\nhttp://heart.com\nhttp://foo.com/\n"
						+ "http://bbc4.com/\nhttp://bbc3.com/\nhttp://bbc1.com/\nhttp://foo.com/contents.html\n"
						+ "http://bbc4.com/menu.html\nhttp://bbc4.com/whatson/\n"
						+ "http://bbc4.com/whatson/thisweek/\nhttp://bbc4.com/whatson/nextweek/\n",
				doTest("http://www.radiolive.com/", 4));
	}

	@Test
	public void testSearch() throws IOException {
		StringWriter output = new StringWriter();
		SearchTestWebCrawler sWebCrawler = new SearchTestWebCrawler(
				"http://www.radiolive.com/", 4, output,
				new LinkedList<Candidate>(), new HashSet<String>(),
				new URLFetcherMock());
		sWebCrawler.crawl();
		assertEquals(
				"All urls must finish with /",
				"http://www.radiolive.com/\nhttp://bbc.com/\nhttp://foo.com/\n"
						+ "http://bbc4.com/\nhttp://bbc3.com/\nhttp://bbc1.com/\n"
						+ "http://bbc4.com/whatson/\n"
						+ "http://bbc4.com/whatson/thisweek/\nhttp://bbc4.com/whatson/nextweek/\n",
				output.toString());
		
		
	}
	

}
