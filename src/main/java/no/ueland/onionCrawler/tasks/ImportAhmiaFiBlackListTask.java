package no.ueland.onionCrawler.tasks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.google.inject.Inject;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;
import no.ueland.onionCrawler.services.ban.BanService;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.http.HTTPFetcherService;
import no.ueland.onionCrawler.services.robotsTxt.RobotsTxtService;
import org.apache.log4j.Logger;

public class ImportAhmiaFiBlackListTask implements Task {

	private Logger logger = Logger.getLogger(CrawlerTask.class);

	@Inject
	private ConfigurationService configurationService;
	@Inject
	private RobotsTxtService robotsTxtService;
	@Inject
	private HTTPFetcherService httpFetcherService;
	@Inject
	private BanService banService;

	private Timer timer;
	private final String blackListURL = "https://ahmia.fi/blacklist/banned/";

	@Override
	public void init() throws OnionCrawlerException {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				doImport();
			}
		}, 120000, 86500000); // Run a while after startup, then daily(ish)
	}

	private void doImport() {
		boolean canRun = configurationService.get().getBoolean(ConfigurationKey.importBlacklistFromAhmiaFi);
		try {
			httpFetcherService.haveTorConnectivity();
		} catch (OnionCrawlerException e) {
			logger.info("Not connected to the Tor-network, awaits updating of blacklist from Ahmia.fi.");
			return;
		}
		if(canRun) {
			// Ok as of robots.txt?
			try {
				if(robotsTxtService.canCrawl(blackListURL)) {
					HTTPFetchResult blackListData = httpFetcherService.fetch(new URL(blackListURL), HTTPMethod.GET);
					String[] bannedDomains = blackListData.getResult().split("\n");
					for(String s : bannedDomains) {
						if(s.length() == 32) { // Simple verification of md5 hash
							banService.addMd5sum(s, "Imported from "+blackListURL);
						}
					}
				}
			} catch (OnionCrawlerException|MalformedURLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void shutdown() throws OnionCrawlerException {
		timer.cancel();
	}

	@Override
	public String getTaskName() {
		return "Imports blacklis from ahmia.fi daily";
	}
}
