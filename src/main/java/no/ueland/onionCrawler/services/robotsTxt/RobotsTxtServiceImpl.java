package no.ueland.onionCrawler.services.robotsTxt;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;
import no.ueland.onionCrawler.objects.robotsTxt.RobotsTxt;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.database.DatabaseService;
import no.ueland.onionCrawler.services.http.HTTPFetcherService;
import no.ueland.onionCrawler.services.onionHost.OnionHostService;
import no.ueland.onionCrawler.utils.OnionHostUtil;
import no.ueland.onionCrawler.utils.URLUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TorHenning on 20.08.2015.
 */
@Singleton
public class RobotsTxtServiceImpl implements RobotsTxtService {

    @Inject
    DatabaseService databaseService;
    @Inject
    OnionHostService onionHostService;
    @Inject
    ConfigurationService configurationService;
    @Inject
    HTTPFetcherService httpFetcherService;

    @Override
    public boolean canCrawl(String URL) throws OnionCrawlerException {

        RobotsTxt robotsTxt = get(URL);

        //Check cached data
        if(robotsTxt.getContent().length() == 0) {
            return true; //No robots.txt defined, crawl away!
        }

        SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
        try {
            URL uriObj = new URL(URL);
            if(uriObj.getHost() == null || uriObj.getHost().length() == 0) {
                throw new OnionCrawlerException("Missing host in URL: "+URL);
            }
            BaseRobotRules rules = robotParser.parseContent(getRobotsTxtURL(uriObj), robotsTxt.getContent().getBytes(), "text/plain", configurationService.get().getString(ConfigurationKey.CrawlerUserAgent));
            return rules.isAllowed(URL);
        } catch (Exception e) {
            throw new OnionCrawlerException(e);
        }
    }

    private RobotsTxt get(String URL) throws OnionCrawlerException {

        URL uriObj = null;
        try {
            uriObj = new URL(URL);
        } catch (MalformedURLException e) {
            throw new OnionCrawlerException(e);
        }

        RobotsTxt res = _get(uriObj);
        if(res == null) {
            add(uriObj);
        }
        res = _get(uriObj);
        return res;
    }

    private RobotsTxt _get(URL uriObj) throws OnionCrawlerException {
        try {
            return (RobotsTxt) this.databaseService.getQueryRunner().query("SELECT * FROM robotsTxt WHERE host=? AND updated > date_sub(NOW(), INTERVAL 6 HOUR) LIMIT 1 ", new BeanHandler(RobotsTxt.class), uriObj.getHost());
        }catch(Exception ex) {
            throw new OnionCrawlerException(ex);
        }
    }

    private void add(URL uriObj) throws OnionCrawlerException {
        try {
            URL url = new URL(getRobotsTxtURL(uriObj));

            HTTPFetchResult httpResult = httpFetcherService.fetch(url, HTTPMethod.GET);

            if(httpResult.getResponseCode() != 200) {
                if(httpResult.getResponseCode() == 404) {
                    httpResult.setResult("");
                } else {
                    throw new OnionCrawlerException("Unknown response code "+httpResult.getResponseCode());
                }
            }

            if(OnionHostUtil.isOnion(uriObj)) {
                onionHostService.setStatus(uriObj.getHost(), true);
            }
            this.databaseService.getQueryRunner().update("REPLACE INTO robotsTxt SET host=?, port=?, content=?, updated=NOW()", uriObj.getHost(), URLUtil.getPort(uriObj), httpResult.getResult());

        }catch (Exception e) {
            if(OnionHostUtil.isOnion(uriObj)) {
                onionHostService.setStatus(uriObj.getHost(), false);
            }
            throw new OnionCrawlerException(e);
        }
    }

    private String getRobotsTxtURL(URL uriObj) {
        return uriObj.getProtocol()+"://"+uriObj.getHost()+":"+ URLUtil.getPort(uriObj)+"/robots.txt";
    }
}
