package com.lps.ruleengine.mapper;

import com.lps.ruleengine.dto.CreateDocumentRequest;
import com.lps.ruleengine.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {
    
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Document toEntity(CreateDocumentRequest createDocumentRequest);
    
    CreateDocumentRequest toCreateRequest(Document document);
}
