package no.ueland.onionCrawler.utils;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 25.08.2015.
 */
public class ExceptionUtil {
    public static Throwable getNonOnionCause(Throwable t) {
        if(t.getCause() instanceof OnionCrawlerException) {
            return getNonOnionCause(t.getCause());
        }
        return t.getCause();
    }
}
