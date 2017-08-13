package no.ueland.onionCrawler.utils;

import java.net.URL;

public class OnionHostUtil {
    public static boolean isOnion(URL url) {
        return url.getHost().length() == 22 && url.getHost().endsWith(".onion");
    }
}
