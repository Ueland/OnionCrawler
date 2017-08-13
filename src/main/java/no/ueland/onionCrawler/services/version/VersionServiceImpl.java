package no.ueland.onionCrawler.services.version;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.database.DatabaseService;
import no.ueland.onionCrawler.utils.DBUtil;
import org.apache.commons.io.FileUtils;

@Singleton
public class VersionServiceImpl implements VersionService {

    @Inject
    private DatabaseService db;
    private String version;

    public VersionServiceImpl() throws OnionCrawlerException {
        try {
            version = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("currentVersion.info").getFile()));
        } catch (IOException e) {
            throw new OnionCrawlerException(e);
        }
    }

    @Override
    public boolean needUpdate() throws OnionCrawlerException {
        String dbVersion = DBUtil.getStringValue(this.db.getQueryRunner(), "SELECT version FROM versions ORDER BY installDate DESC LIMIT 1");
        if (dbVersion == null) {
            try {
                this.db.getQueryRunner().update("INSERT INTO version SET version=?, installDate=NOW()", version);
                return needUpdate();
            } catch (SQLException e) {
                throw new OnionCrawlerException(e);
            }
        }
        return dbVersion.equals(version);
    }

    @Override
    public String getVersion() {
        return version;
    }
}
