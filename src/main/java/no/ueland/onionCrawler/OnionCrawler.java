package no.ueland.onionCrawler;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;
import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import no.ueland.onionCrawler.services.configuration.ConfigurationService;
import no.ueland.onionCrawler.services.http.HTTPFetcherService;
import no.ueland.onionCrawler.services.search.SearchService;
import no.ueland.onionCrawler.services.version.VersionService;
import no.ueland.onionCrawler.tasks.AutoPopulateToCrawlTask;
import no.ueland.onionCrawler.tasks.CrawlerTask;
import no.ueland.onionCrawler.tasks.Task;
import no.ueland.onionCrawler.utils.OnionCrawlerServerUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class OnionCrawler {

    private Logger logger = Logger.getLogger(OnionCrawler.class);
    private Injector injector;
    private SearchService searchService;
    private ConfigurationService configurationService;
    private VersionService versionService;
    private HTTPFetcherService httpFetcherService;
    private Server httpServer;
    private List<Task> tasks;

    public OnionCrawler() {
        System.out.println("OnionCrawler starting up");

        //Add shutdown hook
        addShutdownHook();

        //Start Guice
        initGuice();

        //Start Lucene
        logger.info("Starting up search engine");
        initSearchEngine();

        //Start jetty
        logger.info("Starting up Jetty web server");
        initJetty();

        //Test Tor connection
        logger.info("Testing connection to the Tor network");
        testTorConnectivity();

        //Start task scheduler
        logger.info("Starting up task scheduler");
        initTaskScheduler();

        //Everything up
        List<String> serverIPs = OnionCrawlerServerUtil.getServerIP();
        if (serverIPs.size() == 1) {
            System.out.println("OnionCrawler " + versionService.getVersion() + " started, point your browser to "+serverIPs.get(0)+":8080/admin.");
        } else {
            System.out.println("OnionCrawler " + versionService.getVersion() + " started, point your browser to one of the following addresses:");
            for (String IP : serverIPs) {
                System.out.println("http://" + IP + ":8080/admin");
            }
        }
        logger.info("Startup completed");
        keepRunning();
    }

    private void testTorConnectivity() {
       if(configurationService.get().getStrings(ConfigurationKey.SocksProxies).length == 0) {
           System.out.println("No Socks proxies set up yet, cannot connect to the Tor network until that is done");
            return;
       }
       try {
           httpFetcherService.haveTorConnectivity();
           System.out.println("Tor connectivity working");
       } catch(OnionCrawlerException ox) {
           System.out.println("Could not connect to check.torproject.org, is your Socks proxy up and running?");
       }
    }

    private void initTaskScheduler() {
        tasks = new ArrayList<>();

        tasks.add(new CrawlerTask());
        tasks.add(new AutoPopulateToCrawlTask());

        for(Task t : tasks) {
            try {
                injector.injectMembers(t);
                t.init();
            } catch (OnionCrawlerException e) {
                logger.error("Could not start task "+t.getTaskName()+", reason: "+e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        new OnionCrawler();
    }

    private void initSearchEngine() {
        try {
            searchService.init();
        } catch (OnionCrawlerException e) {
            logger.error("Could not start search engine: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private void initJetty() {
        try {
            httpServer = new Server(8080);

            String webDir = getClass().getClassLoader().getResource("src/main/webapp").toExternalForm();
            logger.debug("creating webapp @ " + webDir);

            WebAppContext context = new WebAppContext();
            // This can be your own project's jar file, but the contents should
            // conform to the WAR layout.
            context.setResourceBase(webDir);

            // A WEB-INF/web.xml is required for Servlet 3.0
            context.setDescriptor(webDir + "WEB-INF/web.xml");
            // Initialize the various configurations required to auto-wire up
            // the Servlet 3.0 annotations, descriptors, and fragments
            context.setConfigurations(new Configuration[]{
                    new AnnotationConfiguration(),
                    new WebXmlConfiguration(),
                    new WebInfConfiguration(),
                    new TagLibConfiguration(),
                    new PlusConfiguration(),
                    new MetaInfConfiguration(),
                    new FragmentConfiguration(),
                    new EnvConfiguration()});

            // Specify the context path that you want this webapp to show up as
            context.setContextPath("/");
            // Tell the classloader to use the "server" classpath over the
            // webapp classpath. (this is so that jars and libs in your
            // server classpath are used, requiring no WEB-INF/lib
            // directory to exist)
            context.setParentLoaderPriority(true);
            // Add this webapp to the server
            httpServer.setHandler(context);
            // Start the server thread
            httpServer.start();
        } catch (Exception ex) {
            logger.error("Could not start web-server: " + ex.getMessage(), ex);
            System.exit(1);
        }
    }

    private void keepRunning() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("Starting shutdown procedure");
                try {
                    httpServer.stop();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                logger.info("Stopping task scheduler");
                stopTaskScheduler();
                logger.info("OnionCrawler shutting down");
            }
        });
    }

    private void stopTaskScheduler() {
        if(tasks != null) {
            for (Task t : tasks) {
                try {
                    t.shutdown();
                } catch (OnionCrawlerException e) {
                    logger.error("Could not stop task " + t.getTaskName() + ", reason: " + e.getMessage(), e);
                }
            }
        }
    }

    private void initGuice() {
        try {
            injector = Guice.createInjector(new OnionCrawlerModule());
            searchService = injector.getInstance(SearchService.class);
            versionService = injector.getInstance(VersionService.class);
            configurationService = injector.getInstance(ConfigurationService.class);
            httpFetcherService = injector.getInstance(HTTPFetcherService.class);
        } catch (Exception ex) {
            logger.error("Could not start core system: " + ex.getMessage(), ex);
            System.exit(1);
        }
    }
}
