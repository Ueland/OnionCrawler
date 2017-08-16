package no.ueland.onionCrawler.web.pages.admin;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.britesnow.snow.web.rest.annotation.WebGet;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.objects.configuration.Configuration;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.web.data.WebResponse;
import no.ueland.onionCrawler.web.pages.Page;

@Singleton
public class ConfigurationPage extends Page {

	@WebModelHandler(matches = "/admin/configuration")
	public void doShowConfigurationPage(@WebModel Map m, HttpServletResponse res, @WebParam("saved") String saved,
	                                    @WebParam("hostname") String hostname, @WebParam("port") Integer port,
	                                    @WebParam("username") String username, @WebParam("password") String password,
	                                    @WebParam("database") String database, @WebParam("userAgent") String userAgent,
	                                    @WebParam("socksProxies") String sProxies) throws Exception {

		if (userAgent == null || userAgent.length() == 0) {
			userAgent = "OnionCrawler - https://github.com/Ueland/OnionCrawler";
		}

		if (sProxies != null) {
			sProxies = sProxies.replace("\r\n", "\n").replace("\n", ",").replace(" ", "").trim();
		}

		//Save settings
		if (hostname != null) {
			try {
				Configuration conf = configurationService.get();
				conf.setValue(ConfigurationKey.DatabaseUsername, username);
				conf.setValue(ConfigurationKey.DatabaseHostname, hostname);
				conf.setValue(ConfigurationKey.DatabaseName, database);
				conf.setValue(ConfigurationKey.DatabasePassword, password);
				conf.setValue(ConfigurationKey.DatabasePort, port);
				conf.setValue(ConfigurationKey.CrawlerUserAgent, userAgent);
				conf.setValue(ConfigurationKey.SocksProxies, sProxies);
				configurationService.save(conf);

				//Config saved, also install database?
				if (m.get("isInstalled") != null && ((boolean) m.get("isInstalled")) == false) {
					databaseService.populateDatabase();
				}
				res.sendRedirect("/admin/configuration?saved=ok");
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				ex.printStackTrace();
				throw new OnionCrawlerException(ex);
			}
		}
		Configuration c = configurationService.get();
		for (ConfigurationKey ck : ConfigurationKey.values()) {
			String out = c.getString(ck);
			if (ck == ConfigurationKey.SocksProxies) {
				if (out == null) {
					out = "";
				}
				out = out.replace(",", "\n");
			}
			m.put(ck.name(), out);
		}
		m.put("isSaved", saved);
	}

	@WebGet("/api/admin/configuration/testDatabase")
	public WebResponse testDatabaseConnection(@WebParam("hostname") String host, @WebParam("port") String port, @WebParam("username") String username,
	                                          @WebParam("password") String password, @WebParam("database") String database) {
		try {
			databaseService.test(username, password, host, port, database);
			return WebResponse.success();
		} catch (OnionCrawlerException e) {
			if (e.getCause() != null) {
				return WebResponse.fail(e.getCause());
			}
			return WebResponse.fail(e);
		}
	}
}
