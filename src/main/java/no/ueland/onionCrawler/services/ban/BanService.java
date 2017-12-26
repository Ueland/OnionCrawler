package no.ueland.onionCrawler.services.ban;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that handles bans of onions which should not be visited/crawled
 * by the bot. Sites are stored as md5-hashes (as other services also do),
 * this to avoid making the banlist publicly available for others to see.
 */
public interface BanService {

	/**
	 * Checks if a given hostname (exctracted from URL) is banned or not
	 * @param URL
	 * @return
	 * @throws OnionCrawlerException
	 */
	boolean isBanned(String URL) throws OnionCrawlerException;

	/**
	 * Adds a md5hash of a banned domain to banlist
	 * @param md5Hash
	 * @param description
	 * @throws OnionCrawlerException
	 */
	void addMd5sum(String md5Hash, String description) throws OnionCrawlerException;

	/**
	 * Removes a banned domain given a md5hash
	 * @param md5Hash
	 * @throws OnionCrawlerException
	 */
	void removeMd5sum(String md5Hash) throws OnionCrawlerException;
}
