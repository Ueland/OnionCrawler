package no.ueland.onionCrawler.utils;

import java.net.URL;

/**
 * Created by TorHenning on 20.08.2015.
 */
public class OnionHostUtil {
    public static boolean isOnion(URL url) {
        return url.getHost().length() == 22 && url.getHost().endsWith(".onion");
    }
}
