package no.ueland.onionCrawler.services.configuration;

import no.ueland.onionCrawler.objects.configuration.Configuration;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

import java.io.File;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface ConfigurationService {
    public File getWorkDir();

    public Configuration get();

    public boolean isInstalled();

    public void save(Configuration c) throws OnionCrawlerException;

    public boolean canSaveConfiguration();
}
