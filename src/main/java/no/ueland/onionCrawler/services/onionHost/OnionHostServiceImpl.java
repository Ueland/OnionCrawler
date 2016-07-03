package no.ueland.onionCrawler.services.onionHost;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.database.DatabaseService;

/**
 * Created by TorHenning on 20.08.2015.
 */
@Singleton
public class OnionHostServiceImpl implements OnionHostService {

    @Inject
    private DatabaseService databaseService;

    @Override
    public void setStatus(String host, boolean online) throws OnionCrawlerException {
        try {
            if (online) {
                this.databaseService.getQueryRunner().update("REPLACE INTO onionHosts SET host=?, lastChecked=NOW(), lastOnline=NOW()", host);
            } else {
                this.databaseService.getQueryRunner().update("REPLACE INTO onionHosts SET host=?, lastChecked=NOW()", host);
            }
        }catch(Exception ex) {
            throw new OnionCrawlerException(ex);
        }
    }
}
