package no.ueland.onionCrawler.services.ban;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.database.DatabaseService;
import no.ueland.onionCrawler.utils.DBUtil;
import org.apache.commons.codec.digest.DigestUtils;

@Singleton
public class BanServiceImpl implements BanService {

	@Inject
	private DatabaseService databaseService;

	@Override
	public boolean isBanned(String URL) throws OnionCrawlerException {
		try {
			URI URIObj = new URI(URL);
			String host = URIObj.getHost();
			String md5Sum = DigestUtils.md5Hex(host);
			return DBUtil.getIntValue(databaseService.getQueryRunner(), "SELECT COUNT(*) FROM bannedDomains WHERE hostMd5Sum = '" + md5Sum + "'") > 0;
		} catch (URISyntaxException e) {
			throw new OnionCrawlerException(e);
		}
	}

	@Override
	public void addMd5sum(String md5Hash, String description) throws OnionCrawlerException {
		// Already stored?
		int storedRows = DBUtil.getIntValue(databaseService.getQueryRunner(), "SELECT COUNT(*) FROM bannedDomains WHERE hostMd5Sum = ?", md5Hash);
		if (storedRows == 0) {
			try {
				databaseService.getQueryRunner().update("INSERT INTO bannedDomains SET hostMd5Sum=?, added=NOW(), comment=?", md5Hash, description);
			} catch (SQLException e) {
				throw new OnionCrawlerException(e);
			}
		}
	}
}
