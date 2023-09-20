package tretton37.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PageScraper {
    private PageScraper() {}

    public static PageResource scrapeResources(String pageURL, Set<String> visitedUrls) throws IOException {

        Document doc = Jsoup.connect(pageURL).get();
        PageResource pageResource = PageResource.of(pageURL);
        pageResource.setHtml(doc.html());
        visitedUrls.add(pageURL);

        pageResource.setHtmlLinks(new HashSet<>(scrapeResourceFromPage(doc, PageResourceType.HTML)));
        pageResource.setScriptLinks(filterVisited(visitedUrls, scrapeResourceFromPage(doc, PageResourceType.SCRIPT)));
        pageResource.setImageLinks(filterVisited(visitedUrls, scrapeResourceFromPage(doc, PageResourceType.BINARY)));
        pageResource.setStylesheetLinks(filterVisited(visitedUrls, scrapeResourceFromPage(doc, PageResourceType.STYLESHEET)));

        return pageResource;
    }

    private static HashSet<String> filterVisited(Set<String> visitedUrls, List<String> urls) {
        List<String> filtered = urls.stream().filter(s -> !visitedUrls.contains(s)).toList();
        visitedUrls.addAll(filtered);
        return new HashSet<>(filtered);
    }


    private static List<String> scrapeResourceFromPage(Document document, PageResourceType pageResourceType) {
        return switch (pageResourceType) {
            case STYLESHEET -> getResourceUrls(document, "link", "href");
            case SCRIPT -> getResourceUrls(document, "script", "src");
            case BINARY -> getResourceUrls(document, "img", "src");
            case HTML -> getResourceUrls(document, "a", "href");
        };
    }

    private static List<String> getResourceUrls(Document document, String htmlElement, String attribute) {
        Elements links = document.select(htmlElement);
        return links.stream().map(element -> element.absUrl(attribute)).toList();
    }
}
