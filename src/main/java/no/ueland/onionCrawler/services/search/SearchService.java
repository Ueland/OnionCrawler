package no.ueland.onionCrawler.services.search;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface SearchService {
    public void init() throws OnionCrawlerException;

    public void stop() throws OnionCrawlerException;

    public void persist() throws OnionCrawlerException;
}
