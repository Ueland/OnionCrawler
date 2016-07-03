package no.ueland.onionCrawler.services.onionHost;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 20.08.2015.
 */
public interface OnionHostService {
    void setStatus(String host, boolean online) throws OnionCrawlerException;
}
