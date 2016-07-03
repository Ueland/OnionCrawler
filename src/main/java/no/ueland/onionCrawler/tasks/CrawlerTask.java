package no.ueland.onionCrawler.tasks;

import com.google.inject.Inject;
import no.ueland.onionCrawler.enums.http.HTTPContentType;
import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.crawl.ToCrawl;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;
import no.ueland.onionCrawler.services.ban.BanService;
import no.ueland.onionCrawler.services.crawl.CrawlService;
import no.ueland.onionCrawler.services.http.HTTPFetcherService;
import no.ueland.onionCrawler.services.onionHost.OnionHostService;
import no.ueland.onionCrawler.services.robotsTxt.RobotsTxtService;
import no.ueland.onionCrawler.utils.ExceptionUtil;
import no.ueland.onionCrawler.utils.OnionHostUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 *  Core task that performs the actual crawling for a URL
 *
 * Created by TorHenning on 19.08.2015.
 */
public class CrawlerTask implements Task {

    private Logger logger = Logger.getLogger(CrawlerTask.class);
    @Inject
    CrawlService crawlService;
    @Inject
    BanService banService;
    @Inject
    RobotsTxtService robotsTxtService;
    @Inject
    HTTPFetcherService httpFetcherService;
    @Inject
    OnionHostService onionHostService;

    private int timeInMSBetweenEachCrawl = 10000;
    Timer timer;

    @Override
    public void init() throws OnionCrawlerException {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    doCrawl();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, 5000, timeInMSBetweenEachCrawl);
    }

    private void doCrawl() throws OnionCrawlerException {
        //Find URL to crawl
        ToCrawl todo = crawlService.get();
        if(todo == null) {
            logger.info("Nothing to crawl");
            return;
        }
        logger.info("Crawling "+todo.getURL());

        //Remove from TODO, will be added again if needed
        crawlService.remove(todo);

        //Is URL banned?
        if(banService.isBanned(todo.getURL())) {
            logger.info(todo.getURL()+" i banned, skipping");
            return;
        }

        try {

            //Is URL allowed by robots.txt?
            if(!robotsTxtService.canCrawl(todo.getURL())) {
                return;
            }

            //do a HEAD request to see if we have a nice content type for crawling
            HTTPFetchResult headResult;
            headResult = httpFetcherService.fetch(todo.asURL(), HTTPMethod.HEAD);

            //Save online/offline-status for host
            if(OnionHostUtil.isOnion(todo.asURL())) {
                onionHostService.setStatus(todo.asURL().getHost(), true);
            }

            if(headResult.getContentType() == HTTPContentType.PLAIN_TEXT ||headResult.getContentType() == HTTPContentType.HTML) {

                //Fetch URL
                HTTPFetchResult fetchResult = httpFetcherService.fetch(todo.asURL(), HTTPMethod.GET);
                if(fetchResult.getResponseCode() == 200) { //OK!

                    //Look for other URL`s to save
                    List<String> anchors = getAnchors(fetchResult.getResult(), todo.asURL());
                    if(anchors.size()>0) {
                        saveAnchors(anchors);
                    }

                    if(OnionHostUtil.isOnion(todo.asURL())) {

                        //Extract textual content from URL

                        //Add content to index
                    }

                }
            }
        } catch (MalformedURLException e) {
            throw new OnionCrawlerException(e);
        } catch (OnionCrawlerException oe) {
            Throwable cause = ExceptionUtil.getNonOnionCause(oe);
            if(cause instanceof ConnectException || cause instanceof UnknownHostException) {
                //Could not connect... Save and try again later
                todo.setLastAttempt(new Date());
                todo.setAttempts(todo.getAttempts() + 1);
                crawlService.add(todo);
                onionHostService.setStatus(todo.asURL().getHost(), false);
            } else {
                throw oe;
            }
        }
    }

    private void saveAnchors(List<String> anchors) {
        anchors.forEach(a -> {
            try {
                crawlService.add(a);
            } catch (OnionCrawlerException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    private List<String> getAnchors(String htmlStr, URL url) throws MalformedURLException {
        List<String> anchors = new ArrayList<>();
        Document doc = Jsoup.parse(htmlStr);
        Elements anchorElements = doc.select("a");
        for(int i=0;i<anchorElements.size();i++) {
            Element a = anchorElements.get(i);
            String href = a.attr("href");
            if(href.startsWith("javascript:") || href.startsWith("#") || href.length() < 2) {
                continue;
            }
            if(!href.startsWith("http://") && !href.startsWith("https://")) {
                href = new URL(url, href).toString();
            }
            if(href.startsWith("http://") || href.startsWith("https://")) {
                if(OnionHostUtil.isOnion(new URL(href))) {
                    anchors.add(href);
                }
            } else {
                logger.error("Do not know how to handle anchor URL: \""+href+"\"");
            }
        }
        return anchors;
    }

    @Override
    public void shutdown() throws OnionCrawlerException {
        timer.cancel();
        timer.purge();
    }

    @Override
    public String getTaskName() {
        return "Crawler";
    }
}
