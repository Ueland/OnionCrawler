package no.ueland.onionCrawler.services.version;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface VersionService {
    public boolean needUpdate() throws OnionCrawlerException;

    public String getVersion();
}
