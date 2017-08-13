package no.ueland.onionCrawler.objects.http;

import no.ueland.onionCrawler.enums.http.HTTPContentType;

public class HTTPFetchResult {
    private int responseCode;
    private String result;
    private HTTPContentType contentType;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public HTTPContentType getContentType() {
        return contentType;
    }

    public void setContentType(HTTPContentType contentType) {
        this.contentType = contentType;
    }
}
