package com.lps.ruleengine.mapper;

import com.lps.ruleengine.dto.CreateDocumentRequest;
import com.lps.ruleengine.model.Document;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T08:12:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Homebrew)"
)
@Component
public class DocumentMapperImpl implements DocumentMapper {

    @Override
    public Document toEntity(CreateDocumentRequest createDocumentRequest) {
        if ( createDocumentRequest == null ) {
            return null;
        }

        Document.DocumentBuilder document = Document.builder();

        document.documentId( createDocumentRequest.getDocumentId() );
        document.documentValue( createDocumentRequest.getDocumentValue() );
        document.valueType( createDocumentRequest.getValueType() );

        return document.build();
    }

    @Override
    public CreateDocumentRequest toCreateRequest(Document document) {
        if ( document == null ) {
            return null;
        }

        CreateDocumentRequest.CreateDocumentRequestBuilder createDocumentRequest = CreateDocumentRequest.builder();

        createDocumentRequest.documentId( document.getDocumentId() );
        createDocumentRequest.documentValue( document.getDocumentValue() );
        createDocumentRequest.valueType( document.getValueType() );

        return createDocumentRequest.build();
    }
}
