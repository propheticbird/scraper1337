package tretton37.scraper;

import java.util.HashSet;

public class PageResource {
    private String pageURL;

    private String html;

    private HashSet<String> htmlLinks;

    private HashSet<String> stylesheetLinks;

    private HashSet<String> imageLinks;

    private HashSet<String> scriptLinks;

    public static PageResource of(String url) {
        return new PageResource(url, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public PageResource(String pageURL, HashSet<String> htmlLinks, HashSet<String> stylesheetLinks, HashSet<String> imageLinks, HashSet<String> scriptLinks) {
        this.pageURL = pageURL;
        this.htmlLinks = htmlLinks;
        this.stylesheetLinks = stylesheetLinks;
        this.imageLinks = imageLinks;
        this.scriptLinks = scriptLinks;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public HashSet<String> getStylesheetLinks() {
        return stylesheetLinks;
    }

    public void setStylesheetLinks(HashSet<String> stylesheetLinks) {
        this.stylesheetLinks = stylesheetLinks;
    }

    public HashSet<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(HashSet<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public HashSet<String> getScriptLinks() {
        return scriptLinks;
    }

    public void setScriptLinks(HashSet<String> scriptLinks) {
        this.scriptLinks = scriptLinks;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public HashSet<String> getHtmlLinks() {
        return htmlLinks;
    }

    public void setHtmlLinks(HashSet<String> htmlLinks) {
        this.htmlLinks = htmlLinks;
    }
}
