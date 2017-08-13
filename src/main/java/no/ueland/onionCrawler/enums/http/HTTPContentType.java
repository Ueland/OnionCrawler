package no.ueland.onionCrawler.enums.http;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

public enum HTTPContentType {
    PLAIN_TEXT,
    HTML,
    JSON,
    ;

    public static HTTPContentType from(String contentType) throws OnionCrawlerException {
        if(contentType.startsWith("text/plain")) {
            return PLAIN_TEXT;
        }
        if(contentType.startsWith("text/html")) {
            return HTML;
        }
        if(contentType.startsWith("application/json")) {
            return JSON;
        }
        throw new OnionCrawlerException("Unknown content type: "+contentType);
    }
}
