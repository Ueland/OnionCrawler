package no.ueland.onionCrawler.search;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.search.SearchService;
import no.ueland.onionCrawler.services.search.SearchServiceImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SearchServiceTest {

	private static SearchService searchService;

	@BeforeClass
	public static void init() throws OnionCrawlerException {
		searchService = new SearchServiceImpl();
		searchService.init();
	}

	@AfterClass
	public static void after() throws OnionCrawlerException {
		searchService.stop();
	}

	@Test
	public void test() throws OnionCrawlerException {
		searchService.persist();
	}

}
