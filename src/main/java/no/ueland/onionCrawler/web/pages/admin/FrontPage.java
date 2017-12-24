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
import no.ueland.onionCrawler.services.http.HTTPFetcherService;
import no.ueland.onionCrawler.services.onionHost.OnionHostService;
import no.ueland.onionCrawler.services.search.SearchService;
import no.ueland.onionCrawler.web.pages.Page;

@Singleton
public class FrontPage extends Page {

	@Inject
	CrawlService crawlService;

	@Inject
	HTTPFetcherService httpFetcherService;

	@Inject
	OnionHostService onionHostService;

	@Inject
	SearchService searchService;

	@WebModelHandler(matches = "/admin/")
	public void doShowFrontpage(@WebModel Map m, HttpServletResponse res, @WebParam("URLToCrawl") String URLToCrawl, @WebParam("URLAdded") String URLAdded) throws Exception {

		// Show box for adding a new URL to crawl
		if (URLToCrawl != null && URLToCrawl.length() > 0) {
			try {
				crawlService.add(URLToCrawl);
				res.sendRedirect("/admin/?URLAdded=ok");
				return;
			} catch (OnionCrawlerException ox) {
				m.put("URLToCrawlError", ox.getMessage());
			}
		}
		if (URLAdded != null && URLAdded.length() > 0) {
			m.put("URLAdded", true);
		}

		// Check for Tor-Connectivity
		try {
			httpFetcherService.haveTorConnectivity();
		} catch (OnionCrawlerException oe) {
			m.put("torConnectivityReason", oe.getMessage());
		}
		m.put("knownHosts", onionHostService.count());
		m.put("indexedPages", searchService.search("*:*").getTotalHits());
		m.put("toCrawlSize", crawlService.count());
	}
}
