package tretton37.scraper;

public class Main {
    public static void main(String[] args) {
        BooksScraper scraper = new BooksScraper(10);
        scraper.startScraping("https://books.toscrape.com/index.html", 0);
    }
}