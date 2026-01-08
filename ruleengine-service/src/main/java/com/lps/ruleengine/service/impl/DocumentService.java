package com.lps.ruleengine.service.impl;

import com.lps.ruleengine.dto.CreateDocumentRequest;
import com.lps.ruleengine.model.Document;
import com.lps.ruleengine.repository.DocumentRepository;
import com.lps.ruleengine.service.IDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService implements IDocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public Document createDocument(CreateDocumentRequest request) {
        log.debug("Creating document: {}", request.getDocumentId());
        
        if (documentRepository.existsByDocumentId(request.getDocumentId())) {
            throw new RuntimeException("Document already exists: " + request.getDocumentId());
        }
        
        Document document = Document.builder()
                .documentId(request.getDocumentId())
                .documentValue(request.getDocumentValue())
                .valueType(request.getValueType())
                .build();
        
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> getDocumentById(String documentId) {
        return documentRepository.findByDocumentId(documentId);
    }

    @Override
    public List<Document> getDocumentsByType(Document.ValueType valueType) {
        return documentRepository.findByValueType(valueType);
    }

    @Override
    public Document updateDocument(String documentId, CreateDocumentRequest request) {
        Optional<Document> existingOpt = documentRepository.findByDocumentId(documentId);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Document not found: " + documentId);
        }
        
        Document existing = existingOpt.get();
        existing.setDocumentValue(request.getDocumentValue());
        existing.setValueType(request.getValueType());
        existing.setVersion(existing.getVersion() + 1);
        
        return documentRepository.save(existing);
    }

    @Override
    public void deleteDocument(String documentId) {
        if (!documentRepository.existsByDocumentId(documentId)) {
            throw new RuntimeException("Document not found: " + documentId);
        }
        documentRepository.deleteById(documentId);
    }

    @Override
    public List<Document> getRecentDocuments() {
        return documentRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public Document createOrUpdateDocument(String documentId, Object value) {
        Optional<Document> existingOpt = documentRepository.findByDocumentId(documentId);
        
        if (existingOpt.isPresent()) {
            Document existing = existingOpt.get();
            Document updated = Document.of(documentId, value);
            existing.setDocumentValue(updated.getDocumentValue());
            existing.setValueType(updated.getValueType());
            existing.setVersion(existing.getVersion() + 1);
            return documentRepository.save(existing);
        } else {
            return documentRepository.save(Document.of(documentId, value));
        }
    }
}
