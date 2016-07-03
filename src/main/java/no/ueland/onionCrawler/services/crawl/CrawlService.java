package no.ueland.onionCrawler.services.crawl;

import no.ueland.onionCrawler.objects.crawl.ToCrawl;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface CrawlService {
    void add(String URL) throws OnionCrawlerException;
    void add(ToCrawl todo) throws OnionCrawlerException;
    ToCrawl get() throws OnionCrawlerException;
    void remove(ToCrawl obj) throws OnionCrawlerException;
}
