package no.ueland.onionCrawler.objects.exception;

public class OnionCrawlerException extends Exception {
    public OnionCrawlerException(String msg, Exception exception) {
        super(msg, exception);
    }

    public OnionCrawlerException(String s) {
        super(s);
    }

    public OnionCrawlerException(Exception e) {
        super(e);
    }

    public OnionCrawlerException() {
    }
}