package org.joel.crawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class HTMLReadTest {

	@Test
	public void testParseEmptyInput() {
		Reader source = new StringReader("");
		try {
			assertEquals("Empty input should contain no URLs", 0,
					HTMLRead.getURLs(source, "http://example.com/").size());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
	}

	@Test
	public void testParseNoURLs() {
		Reader source = new StringReader("<html><head></head><body>\n"
				+ "Look, Ma! No URLs!\n" + "</body></html>");
		try {
			assertEquals("Empty input should contain no URLs", 0,
					HTMLRead.getURLs(source, "http://example.com/").size());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
	}

	@Test
	public void testParseNoBase() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href='fun'>fun</a>!\n" + "</body></html>");
		List<String> expected = Arrays.asList(
				new String[]{
					"http://wikipedia.org/bycicle",
					"http://example.com/fun"
					});
		List<String> res = null;
		try {
			res = HTMLRead.getURLs(source, "http://example.com/");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
		assertEquals("Found different URLs", expected, res);
	}

	@Test
	public void testParseDefaultBaseDoesNotEndInSlash() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href='fun'>fun</a>!\n" + "</body></html>");
		List<String> expected = Arrays.asList(
				new String[]{
					"http://wikipedia.org/bycicle",
					"http://example.com/foo/fun"
					});
		List<String> res = null;
		try {
			res = HTMLRead.getURLs(source, "http://example.com/foo/bar.html");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
		assertEquals("Found different URLs", expected, res);
	}

	@Test
	public void testParseOneBase() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "<base href=\"http://ejemplo.com/\"/>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href='fun'>fun</a>!\n" + "</body></html>");
		List<String> expected = Arrays.asList(
				new String[]{
					"http://wikipedia.org/bycicle",
					"http://ejemplo.com/fun"
					});
		List<String> res = null;
		try {
			res = HTMLRead.getURLs(source, "http://example.com/");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
		assertEquals("Found different URLs", expected, res);
	}

	@Test
	public void testParseTwoBases() {
		Reader source = new StringReader(
				"<html><head></head><body>\n"
						+ "<base href=\"http://ejemplo.com/\"/>\n"
						+ "<base href=\"http://not-this-one.com/\"/>\n"
						+ "Riding <a href=\"http://wikipedia.org/bycicle\">bycicles</a> "
						+ "is <a href='fun'>fun</a>!\n" + "</body></html>");
		List<String> expected = Arrays.asList(
				new String[]{
					"http://wikipedia.org/bycicle",
					"http://ejemplo.com/fun"
					});
		List<String> res = null;
		try {
			res = HTMLRead.getURLs(source, "http://example.com/");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
		assertEquals("Found different URLs", expected, res);
	}

	@Test
	public void testParseBadSyntax() {
		Reader source = new StringReader(
						"<   	 base prtf=\"bleh\" href=\"http://ejemplo.com/\"/>\n"
						+ "<  a \n	href=\"http://wikipedia.org/bycicle\"\n"
						+ "<a href='fun'!\n"
						+ "<a > href=\"not-this-one.html\"");
		List<String> expected = Arrays.asList(
				new String[]{
					"http://wikipedia.org/bycicle",
					"http://ejemplo.com/fun"
					});
		List<String> res = null;
		try {
			res = HTMLRead.getURLs(source, "http://example.com/");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException");
		}
		assertEquals("Found different URLs", expected, res);
	}

}
