package no.ueland.onionCrawler.tasks;

import com.google.inject.Inject;
import no.ueland.onionCrawler.objects.crawl.ToCrawl;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.crawl.CrawlService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  Auto populates the database table containing websites to visit if the
 *  table is empty or not yet populated.
 *
 * Created by TorHenning on 03.07.2016.
 */
public class AutoPopulateToCrawlTask implements Task {

    @Inject
    CrawlService crawlService;
    @Inject
    ConfigurationService configurationService;
    private Logger logger = Logger.getLogger(getClass());
    private Timer timer;
    private String[] startURLs = {
            "https://www.reddit.com/r/onions/top/?sort=top&t=month",
    };

    @Override
    public void init() throws OnionCrawlerException {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    populateIfNeeded();
                } catch (OnionCrawlerException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, 500, 3600*1000); //Check DB table status once per hour
    }

    private void populateIfNeeded() throws OnionCrawlerException {
        if(!configurationService.isInstalled()) {
            return;
        }
        if(crawlService.get() == null) {
            for(String URL : startURLs) {
                logger.info("No URLs available for crawling, adding default start URL: "+URL);
                crawlService.add(URL);
            }
        }
    }

    @Override
    public void shutdown() throws OnionCrawlerException {
        timer.cancel();
        timer.purge();
    }

    @Override
    public String getTaskName() {
        return "Crawler auto populator";
    }
}
