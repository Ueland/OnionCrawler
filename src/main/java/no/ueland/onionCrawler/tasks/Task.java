package no.ueland.onionCrawler.tasks;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;

/**
 * Created by TorHenning on 19.08.2015.
 */
public interface Task {
    void init() throws OnionCrawlerException;
    void shutdown() throws OnionCrawlerException;
    String getTaskName();
}
