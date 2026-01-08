package com.lps.ruleengine.adaptor;

import com.lps.ruleengine.dto.CreateDocumentRequest;
import com.lps.ruleengine.model.Document;
import org.springframework.stereotype.Component;

/**
 * Adaptor class for Document entity builder patterns.
 * Encapsulates all Document builder logic in one place.
 */
@Component
public class DocumentAdaptor {

    /**
     * Creates a Document from CreateDocumentRequest using builder pattern.
     *
     * @param request the create document request
     * @return Document entity built from request
     */
    public Document createDocumentFromRequest(CreateDocumentRequest request) {
        return Document.builder()
                .documentId(request.getDocumentId())
                .documentValue(request.getDocumentValue())
                .valueType(request.getValueType())
                .build();
    }

    /**
     * Creates a Document using the static factory method.
     *
     * @param documentId the document ID
     * @param value the document value
     * @return Document entity built from parameters
     */
    public Document createDocumentFromIdAndValue(String documentId, Object value) {
        return Document.of(documentId, value);
    }
}
