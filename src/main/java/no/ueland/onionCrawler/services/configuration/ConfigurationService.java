package no.ueland.onionCrawler.services.configuration;

import java.io.File;

import no.ueland.onionCrawler.objects.configuration.Configuration;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface ConfigurationService {
	public File getWorkDir();

	public Configuration get();

	public boolean isInstalled();

	public void save(Configuration c) throws OnionCrawlerException;

	public boolean canSaveConfiguration();
}
