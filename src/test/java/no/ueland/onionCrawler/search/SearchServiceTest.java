package no.ueland.onionCrawler.search;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import com.google.inject.Inject;
import eu.fabiostrozzi.guicejunitrunner.GuiceJUnitRunner;
import no.ueland.onionCrawler.OnionCrawlerModule;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.search.SearchDocument;
import no.ueland.onionCrawler.objects.search.SearchResult;
import no.ueland.onionCrawler.services.search.SearchService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({OnionCrawlerModule.class})
public class SearchServiceTest {

	@Inject
	private SearchService searchService;

	@Before
	public void before() throws OnionCrawlerException {
		searchService.add(getTestDocument());
		searchService.persist();
	}

	@Test
	public void testSearchPageTitleField() throws OnionCrawlerException {
		SearchResult result = searchService.search("test");
		assertThat(result, notNullValue());
		assertThat(result.getTotalHits(), greaterThan(0l));
	}

	@Test
	public void testSearchHostnameField() throws OnionCrawlerException {
		SearchResult result = searchService.search("testbooktestwwwi");
		assertThat(result, notNullValue());
		assertThat(result.getTotalHits(), greaterThan(0l));
	}

	@Test
	public void testSearcContentField() throws OnionCrawlerException {
		SearchResult result = searchService.search("fish");
		assertThat(result, notNullValue());
		assertThat(result.getTotalHits(), greaterThan(0l));
	}

	@Test
	public void testSearchURLField() throws OnionCrawlerException {
		SearchResult result = searchService.search("test.foo");
		assertThat(result, notNullValue());
		assertThat(result.getTotalHits(), greaterThan(0l));
	}

	@After
	public void after() throws OnionCrawlerException {
		searchService.remove(getTestDocument().getURL());
		searchService.persist();
	}

	private SearchDocument getTestDocument() {
		SearchDocument sd = new SearchDocument();
		sd.setHostname("testbooktestwwwi.onion");
		sd.setPageContent("This is a fish test");
		sd.setPageTitle("This is a test");
		sd.setURL("http://testbooktestwwwi.onion/test.foo");
		return sd;
	}

}
