package no.ueland.onionCrawler.objects.search;

public class SearchDocument {
	private String URL;
	private String hostname;
	private String pageTitle;
	private String pageContent;

	@Override
	public String toString() {
		return URL+' '+hostname+' '+pageTitle+' '+pageContent;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
}
