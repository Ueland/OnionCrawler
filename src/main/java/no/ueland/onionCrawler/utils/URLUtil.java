package no.ueland.onionCrawler.utils;

import java.net.URL;

/**
 * Created by TorHenning on 24.08.2015.
 */
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
