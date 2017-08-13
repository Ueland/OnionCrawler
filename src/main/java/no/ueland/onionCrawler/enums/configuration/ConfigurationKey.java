package no.ueland.onionCrawler.enums.configuration;

public enum ConfigurationKey {

    // Database connection
    DatabaseUsername,
    DatabaseHostname,
    DatabaseName,
    DatabasePort,
    DatabasePassword,

    // Crawler config
    CrawlerUserAgent,

		// Tor settings
    SocksProxies,

		// Blacklist config
		importBlacklistFromAhmiaFi,

}
