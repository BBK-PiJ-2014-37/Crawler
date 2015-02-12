package org.joel.crawler;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class LargeCandidateQueue implements CandidateQueue {
	private Scanner in;
	private Writer out;

	public LargeCandidateQueue(Path pathname) throws IOException {
		out = Files.newBufferedWriter(pathname);
		in = new Scanner(Files.newBufferedReader(pathname));
	}
	
	public void add(Candidate c) {
		try {
			out.write("" + c.level + " " + c.url + "\n");
			out.flush();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	public boolean isEmpty() {
		return !in.hasNextLine() || !in.hasNextInt();
	}

	public Candidate poll() {
		int level = in.nextInt();
		String url = in.next();
		return new Candidate(level, url);
	}

}
