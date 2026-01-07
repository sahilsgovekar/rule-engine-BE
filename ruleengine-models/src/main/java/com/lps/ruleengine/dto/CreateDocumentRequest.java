package com.lps.ruleengine.dto;

import com.lps.ruleengine.model.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new document")
public class CreateDocumentRequest {

    @NotBlank(message = "Document ID is required")
    @Schema(description = "Unique identifier for the document", example = "doc_min_age")
    private String documentId;

    @NotBlank(message = "Document value is required")
    @Schema(description = "The value to be stored", example = "18")
    private String documentValue;

    @NotNull(message = "Value type is required")
    @Schema(description = "Type of the value")
    private Document.ValueType valueType;
}
