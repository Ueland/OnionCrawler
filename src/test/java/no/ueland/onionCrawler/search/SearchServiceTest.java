package no.ueland.onionCrawler.search;

import com.google.inject.Inject;
import eu.fabiostrozzi.guicejunitrunner.GuiceJUnitRunner;
import no.ueland.onionCrawler.OnionCrawlerModule;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.search.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({OnionCrawlerModule.class})
public class SearchServiceTest {

	@Inject
	private SearchService searchService;

	@Test
	public void test() throws OnionCrawlerException {
		searchService.persist();
	}

}
