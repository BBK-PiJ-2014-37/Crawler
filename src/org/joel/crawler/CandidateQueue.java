package org.joel.crawler;

interface CandidateQueue {

	public void add(Candidate c);
	public boolean isEmpty();
	public Candidate poll();
	
}
