package no.ueland.onionCrawler.services.guice;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

import javax.sql.DataSource;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface DatabaseProvider {
    DataSource get() throws OnionCrawlerException;
}
