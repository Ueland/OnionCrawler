package no.ueland.onionCrawler.services.database;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface DatabaseService {
    public void test(String username, String password, String host, String port, String dbName) throws OnionCrawlerException;

    public QueryRunner getQueryRunner() throws OnionCrawlerException;

    public void populateDatabase() throws OnionCrawlerException;

    public Connection getConnection() throws OnionCrawlerException;
}
