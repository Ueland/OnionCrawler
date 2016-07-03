package no.ueland.onionCrawler.services.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.guice.DatabaseProvider;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by TorHenning on 19.08.2015.
 */

@Singleton
public class DatabaseServiceImpl implements DatabaseService {

    @Inject
    ConfigurationService config;
    @Inject
    DatabaseProvider dbProvider;
    Logger logger = Logger.getLogger(DatabaseServiceImpl.class);
    private QueryRunner queryRunner;

    public DatabaseServiceImpl() {
    }

    @Override
    public void test(String username, String password, String host, String port, String dbName) throws OnionCrawlerException {
        if (port == null) {
            port = "3306";
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new OnionCrawlerException("Driver test failed: " + e.getMessage(), e);
        }
        // Setup the connection with the DB
        Connection con;
        try {
            DriverManager.setLoginTimeout(1);
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OnionCrawlerException("Could not connectt to database: " + e.getMessage(), e);
        }
        Statement statement;
        try {
            statement = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OnionCrawlerException("Statement test failed: " + e.getMessage(), e);
        }
        try {
            statement.executeQuery("SHOW TABLES");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OnionCrawlerException("Query test failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QueryRunner getQueryRunner() throws OnionCrawlerException {
        if (queryRunner == null && config.isInstalled()) {
            try {
                this.queryRunner = new QueryRunner(dbProvider.get());
            } catch (OnionCrawlerException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return queryRunner;
    }

    @Override
    public void populateDatabase() throws OnionCrawlerException {
        try {
            File dbFile = new File("src/main/resources/database.sql");
            logger.info("Installing database from file: " + dbFile.getAbsolutePath());
            String SQLInput = FileUtils.readFileToString(dbFile);
            String[] bits = SQLInput.split(";");
            for (String query : bits) {
                query = query.trim();
                if (query.length() > 0) {
                    logger.debug("Executing query: " + query);
                    getQueryRunner().update(query);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new OnionCrawlerException(e);
        }
    }

    @Override
    public Connection getConnection() throws OnionCrawlerException {
        if (!config.isInstalled()) {
            return null;
        }
        try {
            return getQueryRunner().getDataSource().getConnection();
        } catch (SQLException e) {
            throw new OnionCrawlerException(e);
        }
    }
}
