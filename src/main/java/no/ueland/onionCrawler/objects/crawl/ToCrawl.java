package no.ueland.onionCrawler.objects.crawl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by TorHenning on 19.08.2015.
 */
public class ToCrawl {
    private URL urlInstance;
    private String URL;
    private Date added;
    private Date lastAttempt;
    private int attempts;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Date getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(Date lastAttempt) {
        this.lastAttempt = lastAttempt;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public URL asURL() {
        if(urlInstance == null) {
            try {
                urlInstance = new URL(URL);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return urlInstance;
    }
}
