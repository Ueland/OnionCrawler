package no.ueland.onionCrawler.services.onionHost;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.database.DatabaseService;
import no.ueland.onionCrawler.utils.DBUtil;
import org.apache.log4j.Logger;

@Singleton
public class OnionHostServiceImpl implements OnionHostService {

	private Logger logger = Logger.getLogger(getClass());
	@Inject
	private DatabaseService databaseService;

	@Override
	public void setStatus(String host, boolean online) throws OnionCrawlerException {
		try {
			if (online) {
				logger.info("Setting host-status for " + host + " to online");
				this.databaseService.getQueryRunner().update("REPLACE INTO onionHosts SET host=?, lastChecked=NOW(), lastOnline=NOW()", host);
			} else {
				logger.info("Setting host-status for " + host + " to offline");
				this.databaseService.getQueryRunner().update("REPLACE INTO onionHosts SET host=?, lastChecked=NOW()", host);
			}
		} catch (Exception ex) {
			throw new OnionCrawlerException(ex);
		}
	}

	@Override
	public int count() throws OnionCrawlerException {
		return DBUtil.getIntValue(this.databaseService.getQueryRunner(), "SELECT COUNT(host) FROM onionHosts");
	}
}
