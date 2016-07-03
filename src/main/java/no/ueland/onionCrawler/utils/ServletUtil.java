package no.ueland.onionCrawler.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TorHenning on 19.08.2015.
 */
public class ServletUtil {
    public static String getRequestedPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }
}
