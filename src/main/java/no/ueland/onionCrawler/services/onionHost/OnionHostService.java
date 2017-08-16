package no.ueland.onionCrawler.services.onionHost;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface OnionHostService {
	void setStatus(String host, boolean online) throws OnionCrawlerException;
}
