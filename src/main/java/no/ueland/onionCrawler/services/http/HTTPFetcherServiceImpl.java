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
import java.net.*;

/**
 * Created by TorHenning on 25.08.2015.
 */
@Singleton
public class HTTPFetcherServiceImpl implements HTTPFetcherService {

    @Inject
    private ConfigurationService configurationService;
    private long lastConnectivityCheck = 0;
    private boolean lastConnectivityResult = false;

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

    @Override
    public void haveTorConnectivity() throws OnionCrawlerException {
        if(lastConnectivityCheck != 0 && System.currentTimeMillis() - lastConnectivityCheck < 60000) {
            if(!lastConnectivityResult) {
                throw new OnionCrawlerException("No Tor connectivity detected, will retry within 1 minute");
            } else {
                return;
            }
        }
        try {
            HTTPFetchResult res = this.fetch(new URL("https://check.torproject.org/api/ip"), HTTPMethod.GET);
            lastConnectivityResult = res.getResult().contains("true");
            if(!lastConnectivityResult) {
                throw new OnionCrawlerException("No Tor connectivity detected, have you setup a Tor Socks server?");
            }
        } catch (Exception e) {
            throw new OnionCrawlerException("No Tor connectivity detected: "+e.getMessage());
        } finally {
            lastConnectivityCheck = System.currentTimeMillis();
        }
    }

    private HttpURLConnection getHttpURLConnection(URL URL, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URL.openConnection(getProxy());
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", configurationService.get().getString(ConfigurationKey.CrawlerUserAgent));
        connection.connect();
        return connection;
    }

    private Proxy getProxy() throws UnknownHostException {
        String[] servers = this.configurationService.get().getStrings(ConfigurationKey.SocksProxies);
        if(servers.length == 0) {
            return null;
        }
        if(servers.length == 1) {
            return getProxyFromServer(servers[0]);
        }
        return getProxyFromServer(servers[(int) (Math.random()*servers.length)]);
    }

    private Proxy getProxyFromServer(String server) throws UnknownHostException {
        String[] bits = server.split(":");
        int port = Integer.parseInt(bits[1]);
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(InetAddress.getByName(bits[0]), port));
    }
}
