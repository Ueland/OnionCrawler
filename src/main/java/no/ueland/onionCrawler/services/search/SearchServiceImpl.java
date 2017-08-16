package no.ueland.onionCrawler.services.search;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.enums.search.SearchField;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.util.Version;

@Singleton
public class SearchServiceImpl implements SearchService {

	@Inject
	private ConfigurationService configurationService;
	private IndexSearcher searcher;
	private IndexWriter indexWriter;
	private WhitespaceAnalyzer analyzer;
	private QueryParser queryParser;
	private boolean hasInitialized;
	private IndexReader indexReader;
	private Logger logger = Logger.getLogger(SearchServiceImpl.class);
	private File indexDirectory;
	private NativeFSLockFactory lockFactory;

	@Override
	public void init() throws OnionCrawlerException {
		if (hasInitialized) {
			throw new OnionCrawlerException("Search engine already initialized");
		}

		try {
			lockFactory = new NativeFSLockFactory(new File("/tmp"));
			lockFactory.makeLock("onionCrawler");

			//Index directory
			indexDirectory = new File(configurationService.getWorkDir().getAbsolutePath() + "/lucence");
			if (!indexDirectory.exists()) {
				indexDirectory.mkdirs();
			}

			//Index writer
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_1, new SimpleAnalyzer());
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(FSDirectory.open(indexDirectory, lockFactory), indexWriterConfig);
			indexWriter.commit(); //In case of a new/empty index, this will create a new empty index

			//Index reader
			initIndexReaderAndSearcher();

			analyzer = new WhitespaceAnalyzer();
			queryParser = new QueryParser(SearchField.PageContent.name(), analyzer);

			//Index searcher & query parser for the "ID"-field
			//Run a simple test query and print to the log how many documents the index has available
			Query testQuery = queryParser.parse("*:*");
			TotalHitCountCollector collector = new TotalHitCountCollector();
			searcher.search(testQuery, collector);
			logger.info("Initialized search engine index with " + collector.getTotalHits() + " entries in the index.");

		} catch (Exception ex) {
			throw new OnionCrawlerException(ex);
		}
		hasInitialized = true;
	}

	@Override
	public void stop() throws OnionCrawlerException {
		if (!hasInitialized) {
			throw new OnionCrawlerException("Search engine not initialized");
		}
		try {
			persist(); //Make sure to persist any unwritten index changes before stopping
			indexReader.close();
			indexWriter.close();
			lockFactory.clearLock("onionCrawler");
		} catch (IOException ex) {
			throw new OnionCrawlerException(ex);
		}
		hasInitialized = false;
	}

	@Override
	public void persist() throws OnionCrawlerException {
		lazyInit();
		try {
			logger.info("Persisting Lucene index");
			indexWriter.prepareCommit();
			indexWriter.commit();
			initIndexReaderAndSearcher(); //Tell search(ers) to read from the updated index
			logger.info("Persisting Lucene index complete");
		} catch (IOException e) {
			throw new OnionCrawlerException(e.getMessage(), e);
		}
	}

	private void lazyInit() throws OnionCrawlerException {
		if (!hasInitialized) {
			init();
		}
	}

	private synchronized void initIndexReaderAndSearcher() throws OnionCrawlerException {
		try {
			indexReader = DirectoryReader.open(FSDirectory.open(indexDirectory, lockFactory));
			searcher = new IndexSearcher(indexReader);
		} catch (IOException e) {
			throw new OnionCrawlerException(e);
		}
	}
}
