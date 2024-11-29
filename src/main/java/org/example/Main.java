package org.example;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();


        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Java Junior")
                .content("I do a test task")
                .author(DocumentManager.Author.builder().id("adamUUID").name("Adam Lekh").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document documentSaveMethod = documentManager.save(document);
        System.out.println("Saved document " + documentSaveMethod.getId());

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Java"))
                .build();

        List<DocumentManager.Document> results = documentManager.search(request);
        results.forEach(System.out::println);

        Optional<DocumentManager.Document> byId = documentManager.findById(documentSaveMethod.getId());
        System.out.println(byId);

    }
}