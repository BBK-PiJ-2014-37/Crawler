package org.joel.crawler;

import static org.junit.Assert.*;

import org.junit.Test;

public class LargeStringSetTest {

	@Test
	public void testAddContains() {
		StringSet ss = new LargeStringSet();
		assertFalse(ss.contains("foo"));
		ss.add("foo");
		assertTrue(ss.contains("foo"));
		assertFalse(ss.contains("bar"));
		ss.add("bar");
		assertTrue(ss.contains("bar"));
		assertTrue(ss.contains("foo"));		
	}

}
