package no.ueland.onionCrawler.services.version;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that simply checks what version we are running of the software
 * and if the database and application is on the same version.
 */
public interface VersionService {
	boolean needUpdate() throws OnionCrawlerException;
	String getVersion();
}
