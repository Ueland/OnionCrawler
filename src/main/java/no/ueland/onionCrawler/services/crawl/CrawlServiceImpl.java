package no.ueland.onionCrawler.services.crawl;

import java.sql.SQLException;
import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.crawl.ToCrawl;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.database.DatabaseService;
import no.ueland.onionCrawler.utils.DBUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;

@Singleton
public class CrawlServiceImpl implements CrawlService {
	@Inject
	private static DatabaseService db;

	@Override
	public void add(String URL) throws OnionCrawlerException {
		if (!URL.startsWith("http://") && !URL.startsWith("https://")) {
			throw new OnionCrawlerException("Unknown URL scheme specified");
		}
		ToCrawl obj = new ToCrawl();
		obj.setURL(URL);
		obj.setLastAction(new Date());
		add(obj);
	}

	@Override
	public void add(ToCrawl todo) throws OnionCrawlerException {
		try {
			if (DBUtil.getIntValue(this.db.getQueryRunner(), "SELECT COUNT(URL) FROM toCrawl WHERE URL='" + todo.getURL() + "'") > 0) {
				return;
			}
			this.db.getQueryRunner().update("INSERT INTO toCrawl SET URL=?, lastAction=?, attempts=?", todo.getURL(), todo.getLastAction(), todo.getAttempts());
		} catch (SQLException e) {
			throw new OnionCrawlerException(e);
		}
	}

	@Override
	public ToCrawl get() throws OnionCrawlerException {
		try {
			return (ToCrawl) this.db.getQueryRunner().query("SELECT * FROM toCrawl ORDER BY lastAction ASC LIMIT 1", new BeanHandler(ToCrawl.class));
		} catch (SQLException e) {
			throw new OnionCrawlerException(e);
		}
	}

	@Override
	public void remove(ToCrawl obj) throws OnionCrawlerException {
		try {
			this.db.getQueryRunner().update("DELETE FROM toCrawl WHERE URL=? LIMIT 1", obj.getURL());
		} catch (SQLException e) {
			throw new OnionCrawlerException(e);
		}
	}

	@Override
	public int count() throws OnionCrawlerException {
		return DBUtil.getIntValue(this.db.getQueryRunner(), "SELECT COUNT(URL) FROM toCrawl");
	}
}
