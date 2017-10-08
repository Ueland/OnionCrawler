package no.ueland.onionCrawler.services.search;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.search.SearchDocument;
import no.ueland.onionCrawler.objects.search.SearchResult;

/**
 * Service that handles actual search traffic, that is
 * search queries, adding/removing data in the search index etc
 *
 */
public interface SearchService {
	void init() throws OnionCrawlerException;

	void stop() throws OnionCrawlerException;

	/**
	 * Adds a "document" containing a map of given SearchField`s and corresponding values.
	 * If the document at given URL already exists, it will be updated instead.
	 * @throws OnionCrawlerException
	 */
	void add(SearchDocument document) throws OnionCrawlerException;

	/**
	 * Performs a search query against Lucene
	 * @param query
	 * @throws OnionCrawlerException
	 */
	SearchResult search(String query) throws OnionCrawlerException;

	/**
	 * Removes a document with the given URL from Lucene
	 * @param URL
	 * @throws OnionCrawlerException
	 */
	void remove(String URL) throws OnionCrawlerException;

	void persist() throws OnionCrawlerException;
}
