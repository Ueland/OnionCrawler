package no.ueland.onionCrawler.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
    public static String getRequestedPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }
}
