package org.joel.crawler;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class HTMLReadTest {

	@Test
	public void testParseEmptyInput() {
		Reader source = new StringReader("");
		assertEquals("Empty input should contain no URLs", 0,
				HTMLRead.getURLs(source, "http://example.com/").length);
	}

	@Test
	public void testParseNoURLs() {
		Reader source = new StringReader("<html><head></head><body>\n"
				+ "Look, Ma! No URLs!\n" + "</body></html>");
		assertEquals("Empty input should contain no URLs", 0,
				HTMLRead.getURLs(source, "http://example.com/").length);
	}

	@Test
	public void testParseNoBase() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href=\"fun\">fun</a>!\n" + "</body></html>");
		String[] expected = { "http://wikipedia.org/bycicle",
				"http://example.com/fun" };
		assertEquals("Found wrong number of URLs", 2,
				HTMLRead.getURLs(source, "http://example.com/").length);
		assertEquals("Found different URLs", expected,
				HTMLRead.getURLs(source, "http://example.com/").length);
	}

	@Test
	public void testParseDefaultBaseDoesNotEndInSlash() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href=\"fun\">fun</a>!\n" + "</body></html>");
		String[] expected = { "http://wikipedia.org/bycicle",
				"http://example.com/foo/fun" };
		assertEquals("Found wrong number of URLs", 2,
				HTMLRead.getURLs(source, "http://example.com/").length);
		assertEquals(
				"Found different URLs",
				expected,
				HTMLRead.getURLs(source, "http://example.com/foo/bar.html").length);
	}

	@Test
	public void testParseOneBase() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "<base url=\"http://ejemplo.com/\"/>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href=\"fun\">fun</a>!\n" + "</body></html>");
		String[] expected = { "http://wikipedia.org/bycicle",
				"http://ejemplo.com/fun" };
		assertEquals("Found wrong number of URLs", 2,
				HTMLRead.getURLs(source, "http://example.com/").length);
		assertEquals("Found different URLs", expected,
				HTMLRead.getURLs(source, "http://example.com/").length);
	}

	@Test
	public void testParseTwoBases() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "<base url=\"http://ejemplo.com/\"/>\n"
						+ "<base url=\"http://not-this-one.com/\"/>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href=\"fun\">fun</a>!\n" + "</body></html>");
		String[] expected = { "http://wikipedia.org/bycicle",
				"http://ejemplo.com/fun" };
		assertEquals("Found wrong number of URLs", 2,
				HTMLRead.getURLs(source, "http://example.com/").length);
		assertEquals("Found different URLs", expected,
				HTMLRead.getURLs(source, "http://example.com/").length);
	}

}
