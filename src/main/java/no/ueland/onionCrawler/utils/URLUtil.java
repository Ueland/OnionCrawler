package no.ueland.onionCrawler.utils;

import java.net.URL;

public class URLUtil {
    public static int getPort(URL ob) {
        if(ob.getPort()>0) {
            return ob.getPort();
        }
        if(ob.getProtocol().equals("https")) {
            return 443;
        }
        return 80;
    }
}
