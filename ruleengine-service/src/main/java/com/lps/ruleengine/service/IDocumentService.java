package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.CreateDocumentRequest;
import com.lps.ruleengine.model.Document;

import java.util.List;
import java.util.Optional;

/**
 * Interface for document management operations.
 * Defines contract for CRUD operations and document-specific functionality.
 */
public interface IDocumentService {

    /**
     * Creates a new document
     * @param request The document creation request
     * @return The created document
     * @throws RuntimeException if document already exists
     */
    Document createDocument(CreateDocumentRequest request);

    /**
     * Retrieves all documents
     * @return List of all documents
     */
    List<Document> getAllDocuments();

    /**
     * Retrieves a document by ID
     * @param documentId The document identifier
     * @return Optional containing the document if found
     */
    Optional<Document> getDocumentById(String documentId);

    /**
     * Retrieves documents by value type
     * @param valueType The value type to filter by
     * @return List of documents with the specified type
     */
    List<Document> getDocumentsByType(Document.ValueType valueType);

    /**
     * Updates an existing document
     * @param documentId The document identifier
     * @param request The update request
     * @return The updated document
     * @throws RuntimeException if document not found
     */
    Document updateDocument(String documentId, CreateDocumentRequest request);

    /**
     * Deletes a document
     * @param documentId The document identifier
     * @throws RuntimeException if document not found
     */
    void deleteDocument(String documentId);

    /**
     * Retrieves recent documents
     * @return List of recently created documents (top 10)
     */
    List<Document> getRecentDocuments();

    /**
     * Creates a new document or updates existing one
     * @param documentId The document identifier
     * @param value The document value
     * @return The created or updated document
     */
    Document createOrUpdateDocument(String documentId, Object value);
}
