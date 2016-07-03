package no.ueland.onionCrawler.services.ban;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface BanService {
    boolean isBanned(String URL) throws OnionCrawlerException;

}
