package no.ueland.onionCrawler.services.configuration;

import com.google.inject.Singleton;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.objects.configuration.Configuration;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService {

	private final File workDir = new File("onionCrawler");
	private final File configFile = new File(workDir.getAbsolutePath() + "/settings.ini");
	Logger logger = Logger.getLogger(ConfigurationServiceImpl.class);

	public ConfigurationServiceImpl() {

	}

	@Override
	public File getWorkDir() {
		return workDir;
	}

	@Override
	public Configuration get() {
		Configuration c = new Configuration();
		HashMap<String, Object> values = new HashMap<String, Object>();
		if (configFile.exists()) {
			try {
				List<String> configLines = FileUtils.readLines(configFile, "utf-8");
				configLines.forEach(configLine -> {
					if (configLine.startsWith("#")) {
						return;
					}
					if (!configLine.contains("=")) {
						return;
					}
					String[] bits = configLine.split("=");
					String key = bits[0].trim();
					String val = bits[1].trim();
					values.put(key, val);
				});
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		c.setValues(values);
		return c;
	}

	@Override
	public boolean isInstalled() {
		if (!this.workDir.isDirectory()) {
			return false;
		}
		if (!this.configFile.isFile()) {
			return false;
		}
		return true;
	}

	@Override
	public void save(Configuration c) throws OnionCrawlerException {
		if (c == null) {
			throw new OnionCrawlerException("Configuration cannot be null");
		}
		if (!canSaveConfiguration()) {
			throw new OnionCrawlerException("Cannot save configuration, check permissions for the folder " + this.workDir.getAbsolutePath());
		}
		if (!this.workDir.isDirectory()) {
			this.workDir.mkdir();
		}
		if (!this.configFile.exists()) {
			try {
				this.configFile.createNewFile();
			} catch (IOException e) {
				throw new OnionCrawlerException("Could not create configuration file " + this.configFile.getAbsolutePath(), e);
			}
		}

		try {
			String config = "";
			for (ConfigurationKey key : ConfigurationKey.values()) {
				Object val = null;
				if (c.getObject(key) != null) {
					val = c.getObject(key);
				}
				config += key + "\t=\t" + val + "\r\n";
			}
			config += "\r\n#last updated from OnionCrawler service: " + new Date();
			FileUtils.writeStringToFile(this.configFile, config, "utf-8");
		} catch (Exception e) {
			throw new OnionCrawlerException(e);
		}
	}

	@Override
	public boolean canSaveConfiguration() {
		if (!this.workDir.exists()) {
			return this.workDir.mkdir();
		}
		if (!this.configFile.isFile()) {
			return this.configFile.getParentFile().canWrite();
		}
		return this.configFile.canWrite();
	}
}
