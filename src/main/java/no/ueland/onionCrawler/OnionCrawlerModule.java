package no.ueland.onionCrawler;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import no.ueland.onionCrawler.services.ban.BanService;
import no.ueland.onionCrawler.services.ban.BanServiceImpl;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.configuration.ConfigurationServiceImpl;
import no.ueland.onionCrawler.services.crawl.CrawlService;
import no.ueland.onionCrawler.services.crawl.CrawlServiceImpl;
import no.ueland.onionCrawler.services.database.DatabaseProviderImpl;
import no.ueland.onionCrawler.services.database.DatabaseService;
import no.ueland.onionCrawler.services.database.DatabaseServiceImpl;
import no.ueland.onionCrawler.services.guice.DatabaseProvider;
import no.ueland.onionCrawler.services.http.HTTPFetcherService;
import no.ueland.onionCrawler.services.http.HTTPFetcherServiceImpl;
import no.ueland.onionCrawler.services.onionHost.OnionHostService;
import no.ueland.onionCrawler.services.onionHost.OnionHostServiceImpl;
import no.ueland.onionCrawler.services.robotsTxt.RobotsTxtService;
import no.ueland.onionCrawler.services.robotsTxt.RobotsTxtServiceImpl;
import no.ueland.onionCrawler.services.search.SearchService;
import no.ueland.onionCrawler.services.search.SearchServiceImpl;
import no.ueland.onionCrawler.services.version.VersionService;
import no.ueland.onionCrawler.services.version.VersionServiceImpl;

public class OnionCrawlerModule extends AbstractModule {
	@Override
	protected void configure() {
		Map<Class<?>, Class<?>> serviceClasses = new HashMap<>();
		serviceClasses.put(ConfigurationService.class, ConfigurationServiceImpl.class);
		serviceClasses.put(HTTPFetcherService.class, HTTPFetcherServiceImpl.class);
		serviceClasses.put(RobotsTxtService.class, RobotsTxtServiceImpl.class);
		serviceClasses.put(DatabaseProvider.class, DatabaseProviderImpl.class);
		serviceClasses.put(DatabaseService.class, DatabaseServiceImpl.class);
		serviceClasses.put(OnionHostService.class, OnionHostServiceImpl.class);
		serviceClasses.put(VersionService.class, VersionServiceImpl.class);
		serviceClasses.put(SearchService.class, SearchServiceImpl.class);
		serviceClasses.put(CrawlService.class, CrawlServiceImpl.class);
		serviceClasses.put(BanService.class, BanServiceImpl.class);

		for(Class o: serviceClasses.keySet()) {
			requestStaticInjection(serviceClasses.get(o));
			bind(o).to(serviceClasses.get(o)).asEagerSingleton();
		}
	}
}
