package no.ueland.onionCrawler.services.version;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface VersionService {
    public boolean needUpdate() throws OnionCrawlerException;

    public String getVersion();
}
