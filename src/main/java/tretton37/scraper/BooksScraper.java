package tretton37.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BooksScraper {

    private static final int MAX_DEPTH = 50;
    private final Set<String> visitedUrls = new HashSet<>();

    private final ExecutorService exec;

    public BooksScraper(int numberOfThreads) {
         this.exec = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void startScraping(String url, int depth) {
        if (visitedUrls.contains(url)) {
            return;
        }

        if(MAX_DEPTH == depth) {
            System.out.println("Shutting down thread pool");
            shutdownAndAwaitTermination(exec);
            System.exit(0);
        }

        System.out.println("Scraping URL: " + url);
        System.out.println("Scrape progress: %d%%".formatted(depth * 2));

        try {
            PageResource pageResource = PageScraper.scrapeResources(url, visitedUrls);
            PageSerializer.serialize(pageResource);

            for (String link : pageResource.getHtmlLinks()) {
                exec.submit(new Runnable() {
                    @Override
                    public void run() {
                        startScraping(link, depth + 1);
                    }
                });
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
