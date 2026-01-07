package com.lps.ruleengine.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Document {

    @Id
    @Column(name = "document_id")
    private String documentId;

    @Column(name = "document_value", columnDefinition = "TEXT")
    private String documentValue;

    @Column(name = "value_type")
    @Enumerated(EnumType.STRING)
    private ValueType valueType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @Builder.Default
    private Integer version = 1;

    public enum ValueType {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        LIST,
        OBJECT
    }

    // Helper methods for type conversion
    public Object getTypedValue() {
        if (documentValue == null) return null;
        
        try {
            return switch (valueType) {
                case STRING -> documentValue;
                case INTEGER -> Integer.parseInt(documentValue);
                case DOUBLE -> Double.parseDouble(documentValue);
                case BOOLEAN -> Boolean.parseBoolean(documentValue);
                case LIST -> {
                    ObjectMapper mapper = new ObjectMapper();
                    yield mapper.readValue(documentValue, List.class);
                }
                case OBJECT -> {
                    ObjectMapper mapper = new ObjectMapper();
                    yield mapper.readValue(documentValue, Object.class);
                }
            };
        } catch (JsonProcessingException | NumberFormatException e) {
            throw new RuntimeException("Failed to convert document value: " + e.getMessage(), e);
        }
    }

    public static Document of(String id, Object value) {
        String stringValue;
        ValueType type;

        if (value == null) {
            stringValue = null;
            type = ValueType.STRING;
        } else if (value instanceof String) {
            stringValue = (String) value;
            type = ValueType.STRING;
        } else if (value instanceof Integer) {
            stringValue = value.toString();
            type = ValueType.INTEGER;
        } else if (value instanceof Double) {
            stringValue = value.toString();
            type = ValueType.DOUBLE;
        } else if (value instanceof Boolean) {
            stringValue = value.toString();
            type = ValueType.BOOLEAN;
        } else if (value instanceof List) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                stringValue = mapper.writeValueAsString(value);
                type = ValueType.LIST;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize list value", e);
            }
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                stringValue = mapper.writeValueAsString(value);
                type = ValueType.OBJECT;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize object value", e);
            }
        }

        return Document.builder()
                .documentId(id)
                .documentValue(stringValue)
                .valueType(type)
                .build();
    }
}
