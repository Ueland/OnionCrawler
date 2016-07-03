package no.ueland.onionCrawler;

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

/**
 * Created by TorHenning on 19.08.2015.
 */
public class OnionCrawlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConfigurationService.class).to(ConfigurationServiceImpl.class).asEagerSingleton();
        bind(HTTPFetcherService.class).to(HTTPFetcherServiceImpl.class).asEagerSingleton();
        bind(RobotsTxtService.class).to(RobotsTxtServiceImpl.class).asEagerSingleton();
        bind(DatabaseProvider.class).to(DatabaseProviderImpl.class).asEagerSingleton();
        bind(OnionHostService.class).to(OnionHostServiceImpl.class).asEagerSingleton();
        bind(DatabaseService.class).to(DatabaseServiceImpl.class).asEagerSingleton();
        bind(VersionService.class).to(VersionServiceImpl.class).asEagerSingleton();
        bind(SearchService.class).to(SearchServiceImpl.class).asEagerSingleton();
        bind(CrawlService.class).to(CrawlServiceImpl.class).asEagerSingleton();
        bind(BanService.class).to(BanServiceImpl.class).asEagerSingleton();;
    }
}
