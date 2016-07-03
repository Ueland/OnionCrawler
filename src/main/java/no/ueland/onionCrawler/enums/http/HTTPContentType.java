package no.ueland.onionCrawler.enums.http;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 25.08.2015.
 */
public enum HTTPContentType {
    PLAIN_TEXT,
    HTML,
    ;

    public static HTTPContentType from(String contentType) throws OnionCrawlerException {
        if(contentType.startsWith("text/plain")) {
            return PLAIN_TEXT;
        }
        if(contentType.startsWith("text/html")) {
            return HTML;
        }
        throw new OnionCrawlerException("Unknown content type: "+contentType);
    }
}
