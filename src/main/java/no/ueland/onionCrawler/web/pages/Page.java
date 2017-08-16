package no.ueland.onionCrawler.web.pages;

import com.google.inject.Inject;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.database.DatabaseService;
import org.apache.log4j.Logger;

public class Page {

	protected Logger logger = Logger.getLogger(Page.class);

	@Inject
	protected
	ConfigurationService configurationService;

	@Inject
	protected
	DatabaseService databaseService;
}
