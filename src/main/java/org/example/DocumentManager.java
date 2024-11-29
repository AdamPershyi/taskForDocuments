package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {
    private final Map<String, Document> storage = new HashMap<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
        }
        storage.put(document.getId(), document);

        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {

        return storage.values().stream()
                .filter(doc -> filterByTitlePrefix(doc, request.getTitlePrefixes()))
                .filter(doc -> filterByContainsContents(doc, request.getContainsContents()))
                .filter(doc -> filterByAuthorIds(doc, request.getAuthorIds()))
                .filter(doc -> filterByCreatedRange(doc, request.getCreatedFrom(), request.getCreatedTo()))
                .collect(Collectors.toList());
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {

        return Optional.ofNullable(storage.get(id));
    }

    private boolean filterByTitlePrefix(Document document, List<String> prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return true;
        }

        return prefix.stream()
                .anyMatch(titlePrefix -> document.getTitle() != null && document.getTitle().startsWith(titlePrefix));
    }

    private boolean filterByContainsContents(Document document, List<String> containsContents) {
        if (containsContents == null || containsContents.isEmpty()) {
            return true;
        }

        return containsContents.stream()
                .anyMatch(content -> document.getContent() != null && document.getContent().contains(content));
    }

    private boolean filterByAuthorIds(Document document, List<String> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return true;
        }

        return authorIds.contains(document.getAuthor().getId());
    }

    private boolean filterByCreatedRange(Document document, Instant createdFrom, Instant createdTo) {
        if (createdFrom != null && document.getCreated().isBefore(createdFrom)) {
            return false;
        }
        if (createdTo != null && document.getCreated().isAfter(createdTo)) {
            return false;
        }

        return true;
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}