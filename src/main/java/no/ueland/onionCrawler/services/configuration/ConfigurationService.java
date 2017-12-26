package no.ueland.onionCrawler.services.configuration;

import java.io.File;

import no.ueland.onionCrawler.objects.configuration.Configuration;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Service that handles getting/setting configuration
 * options in the application. Configuration is stored
 * in [run-dir]/OnionCrawler/settings.ini
 */
public interface ConfigurationService {
	File getWorkDir();

	Configuration get();

	boolean isInstalled();

	void save(Configuration c) throws OnionCrawlerException;

	boolean canSaveConfiguration();
}
