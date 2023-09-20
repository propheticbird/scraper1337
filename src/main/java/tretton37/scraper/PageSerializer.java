package tretton37.scraper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PageSerializer {
    private static final String CWD = "./books_to_scrape";

    public static void serialize(PageResource pageResource) {
        savePage(pageResource.getHtml(), pageResource.getPageURL());

        getLinksAndSave(pageResource.getScriptLinks().stream().filter(s -> !"".equals(s)).toList());
        getLinksAndSave(pageResource.getStylesheetLinks().stream().filter(s -> !"".equals(s)).toList());
        getBinaryAndSave(pageResource.getImageLinks().stream().toList());
    }

    private static void getLinksAndSave(List<String> links) {
        links.forEach(scriptLink -> {
            var client = HttpClient.newHttpClient();

            // create a request
            var request = HttpRequest.newBuilder(URI.create(scriptLink)).build();

            // use the client to send the request
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> savePage(response.body(), scriptLink));
        });
    }

    private static void getBinaryAndSave(List<String> links) {
        links.forEach(binaryLink -> {
            var client = HttpClient.newHttpClient();

            // create a request
            var request = HttpRequest.newBuilder(URI.create(binaryLink)).build();

            // use the client to send the request
            client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .thenAccept(response -> {
                        File file = createFileAndDirectory(binaryLink);
                        try {
                            Files.write(file.toPath(), response.body().readAllBytes());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
    }

    private static void savePage(String textFile, String pageURL) {
        try {
            File file = createFileAndDirectory(pageURL);
            saveFileToDirectory(textFile, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createFileAndDirectory(String pageURL) {
        try {
            URL pageUri = new URL(pageURL);
            // remove initial trailing slash in URL path to avoid putting file into filesystem's root
            String pathToFile = stripTrailingSlash(pageUri.getPath());

            File file = Path.of(CWD).resolve(pathToFile).toFile();

            File parentDirs = file.getParentFile();
            if (parentDirs != null && !parentDirs.exists() && !parentDirs.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parentDirs);
            }
            return file;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveFileToDirectory(String htmlContent, File file) throws IOException {
        // Create a FileWriter
        FileWriter fileWriter = new FileWriter(file);

        try {
            // Write the HTML content to the file
            fileWriter.write(htmlContent);
            System.out.println("Scraper have saved a file: " + file.getPath());
        } finally {
            // Close the FileWriter
            fileWriter.close();
        }
    }

    private static String stripTrailingSlash(String filePath) {
        return filePath.startsWith("/") ? filePath.substring(1) : filePath;
    }
}
