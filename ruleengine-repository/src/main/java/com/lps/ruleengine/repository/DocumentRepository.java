package com.lps.ruleengine.repository;

import com.lps.ruleengine.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    Optional<Document> findByDocumentId(String documentId);

    List<Document> findByValueType(Document.ValueType valueType);

    @Query("SELECT d FROM Document d WHERE d.documentValue = :value")
    List<Document> findByDocumentValue(String value);

    List<Document> findTop10ByOrderByCreatedAtDesc();

    boolean existsByDocumentId(String documentId);
}
