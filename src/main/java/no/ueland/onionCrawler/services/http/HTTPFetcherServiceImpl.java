package no.ueland.onionCrawler.services.http;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.enums.http.HTTPContentType;
import no.ueland.onionCrawler.enums.http.HTTPMethod;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.http.HTTPFetchResult;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TorHenning on 25.08.2015.
 */
@Singleton
public class HTTPFetcherServiceImpl implements HTTPFetcherService {

    @Inject
    private ConfigurationService configurationService;

    @Override
    public HTTPFetchResult fetch(URL URL, HTTPMethod method) throws OnionCrawlerException {
        try {
            StringBuilder content = new StringBuilder();
            HttpURLConnection connection = getHttpURLConnection(URL, method.name());
            HTTPFetchResult result = new HTTPFetchResult();
            result.setResponseCode(connection.getResponseCode());
            result.setContentType(HTTPContentType.from(connection.getContentType()));

            if(method != HTTPMethod.HEAD) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    content.append(output);
                    content.append("\n");
                }
                connection.disconnect();
                result.setResult(content.toString());
            }
            return result;
        }catch(Exception ex) {
            throw new OnionCrawlerException(ex);
        }
    }

    private HttpURLConnection getHttpURLConnection(URL URL, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", configurationService.get().getString(ConfigurationKey.CrawlerUserAgent));
        connection.connect();
        return connection;
    }
}
