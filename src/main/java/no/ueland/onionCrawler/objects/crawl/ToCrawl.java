package no.ueland.onionCrawler.objects.crawl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class ToCrawl {
	private URL urlInstance;
	private String URL;
	private Date lastAction;
	private int attempts;

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public URL asURL() {
		if (urlInstance == null) {
			try {
				urlInstance = new URL(URL);
			} catch (MalformedURLException e) {
				return null;
			}
		}
		return urlInstance;
	}

	public Date getLastAction() {
		return lastAction;
	}

	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}
}
