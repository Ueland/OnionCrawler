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
	void add(String URL) throws OnionCrawlerException;

	void add(ToCrawl todo) throws OnionCrawlerException;

	ToCrawl get() throws OnionCrawlerException;

	void remove(ToCrawl obj) throws OnionCrawlerException;
}
