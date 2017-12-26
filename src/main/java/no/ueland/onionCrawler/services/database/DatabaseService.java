package no.ueland.onionCrawler.services.database;

import java.sql.Connection;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import org.apache.commons.dbutils.QueryRunner;

/**
 * Service that handles database specific actions
 */
public interface DatabaseService {
	void test(String username, String password, String host, String port, String dbName) throws OnionCrawlerException;

	QueryRunner getQueryRunner() throws OnionCrawlerException;

	void populateDatabase() throws OnionCrawlerException;

	Connection getConnection() throws OnionCrawlerException;
}
