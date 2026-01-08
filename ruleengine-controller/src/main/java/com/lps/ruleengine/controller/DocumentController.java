package com.lps.ruleengine.controller;

import com.lps.ruleengine.dto.CreateDocumentRequest;
import com.lps.ruleengine.model.Document;
import com.lps.ruleengine.service.IDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Document Management", description = "APIs for managing documents (reference values)")
public class DocumentController {

    private final IDocumentService documentService;

    @Operation(summary = "Create a new document", description = "Creates a new document with the specified value")
    @PostMapping
    public ResponseEntity<Document> createDocument(@Valid @RequestBody CreateDocumentRequest request) {
        log.info("Creating document: {}", request.getDocumentId());
        try {
            Document document = documentService.createDocument(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        } catch (Exception e) {
            log.error("Error creating document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get all documents", description = "Retrieves all documents in the system")
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Get document by ID", description = "Retrieves a specific document by its ID")
    @GetMapping("/{documentId}")
    public ResponseEntity<Document> getDocumentById(
            @Parameter(description = "Document ID") @PathVariable String documentId) {
        Optional<Document> document = documentService.getDocumentById(documentId);
        return document.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get documents by type", description = "Retrieves documents filtered by value type")
    @GetMapping("/type/{valueType}")
    public ResponseEntity<List<Document>> getDocumentsByType(
            @Parameter(description = "Value type") @PathVariable Document.ValueType valueType) {
        List<Document> documents = documentService.getDocumentsByType(valueType);
        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Update an existing document", description = "Updates an existing document with new value")
    @PutMapping("/{documentId}")
    public ResponseEntity<Document> updateDocument(
            @Parameter(description = "Document ID") @PathVariable String documentId,
            @Valid @RequestBody CreateDocumentRequest request) {
        try {
            Document updatedDocument = documentService.updateDocument(documentId, request);
            return ResponseEntity.ok(updatedDocument);
        } catch (Exception e) {
            log.error("Error updating document: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a document", description = "Deletes a document from the system")
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @Parameter(description = "Document ID") @PathVariable String documentId) {
        try {
            documentService.deleteDocument(documentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting document: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get recent documents", description = "Retrieves recently created documents")
    @GetMapping("/recent")
    public ResponseEntity<List<Document>> getRecentDocuments() {
        List<Document> documents = documentService.getRecentDocuments();
        return ResponseEntity.ok(documents);
    }
}
