package org.joel.crawler;

import java.util.List;

interface URLFetcher {
	List<String> fetch(String url);
}
