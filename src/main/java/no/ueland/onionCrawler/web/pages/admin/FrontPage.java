package no.ueland.onionCrawler.web.pages.admin;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.crawl.CrawlService;
import no.ueland.onionCrawler.web.pages.Page;

@Singleton
public class FrontPage extends Page {

    @Inject
    CrawlService crawlService;

    @WebModelHandler(matches = "/admin/")
    public void doShowConfigurationPage(@WebModel Map m, HttpServletResponse res, @WebParam("URLToCrawl") String URLToCrawl, @WebParam("URLAdded") String URLAdded) throws Exception {
        if(URLToCrawl != null && URLToCrawl.length()>0) {
            try {
                crawlService.add(URLToCrawl);
                res.sendRedirect("/admin/?URLAdded=ok");
                return;
            }catch (OnionCrawlerException ox) {
                m.put("URLToCrawlError", ox.getMessage());
            }
        }
        if(URLAdded != null && URLAdded.length()>0) {
            m.put("URLAdded", true);
        }
    }
}
