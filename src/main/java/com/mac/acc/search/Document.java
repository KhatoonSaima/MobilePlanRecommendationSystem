package com.mac.acc.search;

import com.mac.acc.search.engine.exception.DocumentProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a document in the search engine system, handling both local and remote HTML content.
 * Provides functionality for downloading, parsing, and text extraction from web pages.
 *
 * @author Weiming Zheng
 * @since 2024-11-30
 */
@Slf4j
@Getter
public class Document {
    private final Path path;
    private final String url;
    private String text;

    // Timeout for HTTP connection in milliseconds
    private static final int CONNECT_TIMEOUT = 10000;

    // Maximum file size to download (5MB)
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    public Document(Path path, String url) {
        this.path = path;
        this.url = url;
    }

    public String getText() {
        if (text == null) {
            try {
                // If file doesn't exist, try to download it
                if (!Files.exists(path)) {
                    log.info("Document not found at {}. Attempting to download from {}", path, url);
                    downloadDocument();
                }

                if (!Files.isReadable(path)) {
                    throw new DocumentProcessingException(
                            String.format("Document file not readable: %s", path));
                }

                text = parseDocument();
                if (text.isEmpty()) {
                    log.warn("Empty document content found at: {}", path);
                }
            } catch (IOException e) {
                throw new DocumentProcessingException(
                        String.format("Failed to process document at %s", path), e);
            }
        }
        return text;
    }

    private void downloadDocument() throws IOException {
        // Create parent directories if they don't exist
        Files.createDirectories(path.getParent());

        try {
            // Configure Jsoup connection
            org.jsoup.Connection connection = Jsoup.connect(url)
                    .maxBodySize(MAX_FILE_SIZE)
                    .timeout(CONNECT_TIMEOUT)
                    .followRedirects(true);

            // Download and parse the document
            org.jsoup.nodes.Document doc = connection.get();

            // Configure output settings to preserve original HTML structure
            OutputSettings settings = new OutputSettings();
            settings.prettyPrint(false);
            doc.outputSettings(settings);

            // Write the HTML to file
            Files.writeString(path, doc.html());
            log.info("Successfully downloaded document from {} to {}", url, path);

        } catch (IOException e) {
            log.error("Failed to download document from {}", url, e);
            throw new DocumentProcessingException(
                    String.format("Failed to download document from %s", url), e);
        }
    }

    private String parseDocument() throws IOException {
//        return Jsoup.parse(path.toFile(), "UTF-8").text();
        return Jsoup.parse(path.toFile(), "UTF-8").html();
    }

    public List<String> extractWords() {
        String content = getText();
        return Arrays.stream(content.split("(?<=[a-zA-Z0-9_-])(?=[^a-zA-Z0-9_-])|(?<=[^a-zA-Z0-9_-])(?=[a-zA-Z0-9_-])"))
                .map(String::trim)
                .map(word -> word.replaceAll("[^a-zA-Z0-9_-]", ""))
                .filter(word -> !word.isEmpty())
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(path, document.path) &&
                Objects.equals(url, document.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, url);
    }
}
