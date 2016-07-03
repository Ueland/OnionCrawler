package no.ueland.onionCrawler.services.robotsTxt;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 20.08.2015.
 */
public interface RobotsTxtService {
    boolean canCrawl(String URL) throws OnionCrawlerException;
}
