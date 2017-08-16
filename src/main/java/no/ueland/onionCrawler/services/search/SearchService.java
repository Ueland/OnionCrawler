package no.ueland.onionCrawler.services.search;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface SearchService {
	public void init() throws OnionCrawlerException;

	public void stop() throws OnionCrawlerException;

	public void persist() throws OnionCrawlerException;
}
