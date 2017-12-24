package no.ueland.onionCrawler.services.crawl;

import no.ueland.onionCrawler.objects.crawl.ToCrawl;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that handles saving/getting URLs that
 * the crawler task should work with. The crawler
 * task fetch a URL via this service and adds
 * any URLs found during crawling, given that
 * they are not on a banned onion domian.
 */

public interface CrawlService {

	/**
	 * Add a URL string to crawl
	 * @param URL
	 * @throws OnionCrawlerException
	 */
	void add(String URL) throws OnionCrawlerException;

	/**
	 * Add a ToCrawl object to crawl
	 * @param todo
	 * @throws OnionCrawlerException
	 */
	void add(ToCrawl todo) throws OnionCrawlerException;

	/**
	 * Get a ToCrawl object containing information
	 * about a URL to crawl
	 * @return
	 * @throws OnionCrawlerException
	 */
	ToCrawl get() throws OnionCrawlerException;

	/**
	 * Remove a gvien ToCrawl object
	 * @param obj
	 * @throws OnionCrawlerException
	 */
	void remove(ToCrawl obj) throws OnionCrawlerException;

	/**
	 * Count the number of pages that is waiting to be crawled
	 * @return
	 * @throws OnionCrawlerException
	 */
	int count() throws OnionCrawlerException;
}
