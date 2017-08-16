package no.ueland.onionCrawler.services.robotsTxt;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface RobotsTxtService {
	boolean canCrawl(String URL) throws OnionCrawlerException;
}
