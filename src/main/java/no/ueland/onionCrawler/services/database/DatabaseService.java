package no.ueland.onionCrawler.services.database;

import java.sql.Connection;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import org.apache.commons.dbutils.QueryRunner;

public interface DatabaseService {
    public void test(String username, String password, String host, String port, String dbName) throws OnionCrawlerException;

    public QueryRunner getQueryRunner() throws OnionCrawlerException;

    public void populateDatabase() throws OnionCrawlerException;

    public Connection getConnection() throws OnionCrawlerException;
}
