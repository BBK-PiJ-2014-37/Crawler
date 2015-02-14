package org.joel.crawler;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

public class LargeStringSetTest {

	@Test
	public void testAddContains() {
		StringSet ss = null;
		ss = new LargeStringSet(Paths.get("/tmp/LargeStringSetTest"));
		assertFalse(ss.contains("foo"));
		ss.add("foo");
		assertTrue(ss.contains("foo"));
		assertFalse(ss.contains("bar"));
		ss.add("bar");
		assertTrue(ss.contains("bar"));
		assertTrue(ss.contains("foo"));		
	}

}
