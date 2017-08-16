package no.ueland.onionCrawler.services.guice;

import javax.sql.DataSource;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public interface DatabaseProvider {
	DataSource get() throws OnionCrawlerException;
}
