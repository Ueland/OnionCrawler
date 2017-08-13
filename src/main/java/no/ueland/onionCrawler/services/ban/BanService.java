package no.ueland.onionCrawler.services.ban;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface BanService {
	boolean isBanned(String URL) throws OnionCrawlerException;

	void addMd5sum(String md5Hash, String description) throws OnionCrawlerException;
}
