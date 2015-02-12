package org.joel.crawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class LargeCandidateQueueTest {
	CandidateQueue q;
	
	@Before
	public void setUp() throws IOException {
		q = new LargeCandidateQueue(
				Paths.get("/tmp/LargeCandidateQueueTest"));
	}
	
	@Test
	public void testBeginsEmpty() {
		assertTrue(q.isEmpty());
	}
	
	@Test
	public void testAddPoll() {
		Candidate c1 = new Candidate(0,"http://www.radiolive.com/");
		q.add(c1);
		assertTrue(!q.isEmpty());
		Candidate c2 = q.poll();
		assertTrue(c1.equals(c2));
		assertTrue(q.isEmpty());
	}
	
	@Test
	public void testAddPollSeveralCandidates() {
		Candidate c1 = new Candidate(0,"http://www.radiolive.com/");
		Candidate c2 = new Candidate(1,"http://bbc4.com/whatson/");
		Candidate c3 = new Candidate(2,"http://bbc4.com/whatson/thisweek/");
		q.add(c1);
		q.add(c2);
		q.add(c3);
		Candidate c4 = q.poll();
		Candidate c5 = q.poll();
		Candidate c6 = q.poll();
		assertTrue(c1.equals(c4));
		assertTrue(c2.equals(c5));
		assertTrue(c3.equals(c6));
		assertTrue(q.isEmpty());
	}
}
