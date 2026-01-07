package com.lps.ruleengine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Rule {

    @Id
    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "expression", nullable = false)
    private String expression;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "on_true_type")
    @Enumerated(EnumType.STRING)
    private OutcomeType onTrueType;

    @Column(name = "on_true_value")
    private String onTrueValue;

    @Column(name = "on_false_type")
    @Enumerated(EnumType.STRING)
    private OutcomeType onFalseType;

    @Column(name = "on_false_value")
    private String onFalseValue;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @Builder.Default
    private Integer version = 1;

    public enum OutcomeType {
        RULE,   // Points to another rule
        VALUE   // Terminal value (true/false)
    }

    // Helper methods
    @JsonIgnore
    public boolean getOnTrueValueAsBoolean() {
        if (onTrueType == OutcomeType.VALUE) {
            return Boolean.parseBoolean(onTrueValue);
        }
        throw new IllegalStateException("OnTrue is not a VALUE type");
    }

    @JsonIgnore
    public boolean getOnFalseValueAsBoolean() {
        if (onFalseType == OutcomeType.VALUE) {
            return Boolean.parseBoolean(onFalseValue);
        }
        throw new IllegalStateException("OnFalse is not a VALUE type");
    }

    @JsonIgnore
    public String getOnTrueRuleId() {
        if (onTrueType == OutcomeType.RULE) {
            return onTrueValue;
        }
        throw new IllegalStateException("OnTrue is not a RULE type");
    }

    @JsonIgnore
    public String getOnFalseRuleId() {
        if (onFalseType == OutcomeType.RULE) {
            return onFalseValue;
        }
        throw new IllegalStateException("OnFalse is not a RULE type");
    }
}
