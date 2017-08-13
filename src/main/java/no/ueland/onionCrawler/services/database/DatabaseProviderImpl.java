package no.ueland.onionCrawler.services.database;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.objects.configuration.Configuration;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.guice.DatabaseProvider;
import org.apache.log4j.Logger;

@Singleton
public class DatabaseProviderImpl implements DatabaseProvider {

    private static HikariDataSource datasource = new HikariDataSource();
    private final ConfigurationService configurationService;
    Logger logger = Logger.getLogger(DatabaseProviderImpl.class);
    boolean hasCreatedConnection = false;

    @Inject
    public DatabaseProviderImpl(ConfigurationService c) {
        this.configurationService = c;
    }

    private void setupConnection() {
        Configuration config = configurationService.get();
        try {
            String hostname = config.getString(ConfigurationKey.DatabaseHostname);
            String database = config.getString(ConfigurationKey.DatabaseName);
            String username = config.getString(ConfigurationKey.DatabaseUsername);
            String password = config.getString(ConfigurationKey.DatabasePassword);
            int port = 3306;
            try {
                port = config.getInt(ConfigurationKey.DatabasePort);
            } catch (Exception ex) {
                //ignored
            }
            HikariConfig hconfig = new HikariConfig();
            hconfig.setDriverClassName("com.mysql.jdbc.Driver");
            hconfig.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?rewriteBatchedStatements=true&autoReconnectForPools=true&autoReconnect=true&useEncoding=true&useUnicode=yes&characterEncoding=UTF-8&characterSetResults=UTF-8");
            hconfig.setUsername(username);
            hconfig.setPassword(password);
            hconfig.setConnectionTestQuery("/* OnionCrawler */ SELECT 1");
            hconfig.setAutoCommit(true);
            hconfig.setIdleTimeout(5000);
            hconfig.setMaxLifetime(60000);
            hconfig.setMinimumIdle(1);
            hconfig.setMaximumPoolSize(20);
            hconfig.setLeakDetectionThreshold(10000);
            datasource = new HikariDataSource(hconfig);
            hasCreatedConnection = true;
        } catch (Exception nx) {
            logger.error("Error setting up database connection, please make sure that all database settings are defined in your configuration, #: " + nx.getMessage(), nx);
        }
    }

    @Override
    public DataSource get() throws OnionCrawlerException {
        if (!hasCreatedConnection) {
            setupConnection();
        }
        return datasource;
    }
}
