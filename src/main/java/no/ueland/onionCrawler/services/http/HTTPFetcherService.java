package no.ueland.onionCrawler.services.http;

import java.net.URL;

import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;

public interface HTTPFetcherService {
	HTTPFetchResult fetch(URL URL, HTTPMethod method) throws OnionCrawlerException;

	void haveTorConnectivity() throws OnionCrawlerException;
}
