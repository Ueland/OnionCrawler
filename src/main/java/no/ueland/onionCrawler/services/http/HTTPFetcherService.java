package no.ueland.onionCrawler.services.http;

import java.net.URL;

import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;

/**
 * Service that handles HTTP-traffic, also have a function
 * to check that we have a active/working Tor-connection, this
 * check is done over HTTP towards the Tor-network`s API.
 *
 * Any usage of the fetch method should only be done after
 * haveTorConnectivity() is called to verify that we are not
 * bypassing the Tor-network.
 */
public interface HTTPFetcherService {
	HTTPFetchResult fetch(URL URL, HTTPMethod method) throws OnionCrawlerException;

	void haveTorConnectivity() throws OnionCrawlerException;
}
