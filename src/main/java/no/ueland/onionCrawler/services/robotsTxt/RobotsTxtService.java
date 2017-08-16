package no.ueland.onionCrawler.services.robotsTxt;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that takes care of verifying that a given URL
 * can be crawled as per the site`s robots.txt specs.
 *
 * The robots.txt data is cached in the DB for upwards
 * of 24 hrs to avoid unnessesarry HTTP-traffic.
 */
public interface RobotsTxtService {
	boolean canCrawl(String URL) throws OnionCrawlerException;
}
