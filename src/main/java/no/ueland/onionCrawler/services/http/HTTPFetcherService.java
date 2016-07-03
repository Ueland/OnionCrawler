package no.ueland.onionCrawler.services.http;

import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;

import java.net.URL;

/**
 * Created by TorHenning on 25.08.2015.
 */
public interface HTTPFetcherService {
    HTTPFetchResult fetch(URL URL, HTTPMethod method) throws OnionCrawlerException;
    void haveTorConnectivity() throws OnionCrawlerException;
}
