package no.ueland.onionCrawler.services.search;

import java.io.IOException;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.enums.search.SearchField;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.objects.search.SearchDocument;
import no.ueland.onionCrawler.objects.search.SearchResult;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.store.SimpleFSDirectory;

@Singleton
public class SearchServiceImpl implements SearchService {

	@Inject
	private static ConfigurationService configurationService;
	private static IndexSearcher searcher;
	private static IndexWriter indexWriter;
	private static Analyzer analyzer;
	private static QueryParser queryParser;
	private static boolean hasInitialized;
	private static IndexReader indexReader;
	private static Logger logger = Logger.getLogger(SearchServiceImpl.class);
	private static Directory indexDirectory;
	private static Lock indexLock;

	@Override
	public void init() throws OnionCrawlerException {
		if (hasInitialized) {
			throw new OnionCrawlerException("Search engine already initialized");
		}

		analyzer = new StandardAnalyzer();

		try {
			//Index directory
			indexDirectory = new SimpleFSDirectory(Paths.get(configurationService.getWorkDir().getAbsolutePath() + "/lucence"));

			// Lock it
			indexLock = NativeFSLockFactory.INSTANCE.obtainLock(indexDirectory, "onionCrawler");

			//Index writer
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
			indexWriter.commit(); //In case of a new/empty index, this will create a new empty index

			//Index reader
			initIndexReaderAndSearcher();

			queryParser = new QueryParser(SearchField.Text.name(), analyzer);
			queryParser.setSplitOnWhitespace(true);
			queryParser.setAutoGeneratePhraseQueries(true);
			queryParser.setDefaultOperator(QueryParser.Operator.OR);

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
			indexLock.close();
		} catch (IOException ex) {
			throw new OnionCrawlerException(ex);
		}
		hasInitialized = false;
	}

	@Override
	public void add(SearchDocument document) throws OnionCrawlerException {
		lazyInit();
		Document doc = new Document();
		doc.add(SearchField.URL.getField(document.getURL()));
		doc.add(SearchField.Hostname.getField(document.getHostname()));
		doc.add(SearchField.PageTitle.getField(document.getPageTitle()));
		doc.add(SearchField.Text.getField(document.toString()));
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			throw new OnionCrawlerException(e);
		}
	}

	@Override
	public SearchResult search(String searchTerms) throws OnionCrawlerException {
		lazyInit();
		SearchResult result = new SearchResult();
		try {
			Query query = queryParser.parse(searchTerms);
			logger.debug("Executing query: "+query.toString());
			TopDocs hits = searcher.search(query, 25);
			result.setTotalHits(hits.totalHits);

		} catch (IOException|ParseException e) {
			throw new OnionCrawlerException(e);
		}
		return result;
	}

	@Override
	public void remove(String URL) throws OnionCrawlerException {
		lazyInit();
		try {
			indexWriter.deleteDocuments(new Term(SearchField.URL.name(), URL));
		} catch (IOException e) {
			throw new OnionCrawlerException(e);
		}
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
			indexReader = DirectoryReader.open(indexDirectory);
			searcher = new IndexSearcher(indexReader);

			int n = indexReader.getDocCount(SearchField.URL.name());
			logger.info("Searched reloaded, index alive with "+n+" docs");
		} catch (IOException e) {
			throw new OnionCrawlerException(e);
		}
	}
}
