package no.ueland.onionCrawler.services.ban;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that handles bans of onions which should not be visited/crawled
 * by the bot. Sites are stored as md5-hashes (as other services also do),
 * this to avoid making the banlist publicly available for others to see.
 */
public interface BanService {
	boolean isBanned(String URL) throws OnionCrawlerException;

	void addMd5sum(String md5Hash, String description) throws OnionCrawlerException;
}
