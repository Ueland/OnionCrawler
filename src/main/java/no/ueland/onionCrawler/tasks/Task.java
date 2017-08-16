package no.ueland.onionCrawler.tasks;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface Task {
	void init() throws OnionCrawlerException;

	void shutdown() throws OnionCrawlerException;

	String getTaskName();
}
