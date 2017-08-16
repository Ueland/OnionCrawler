package no.ueland.onionCrawler.services.search;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that handles actual search traffic, that is
 * search queries, adding/removing data in the search index etc
 *
 * (Still in TODO)
 */
public interface SearchService {
	void init() throws OnionCrawlerException;

	void stop() throws OnionCrawlerException;

	void persist() throws OnionCrawlerException;
}
