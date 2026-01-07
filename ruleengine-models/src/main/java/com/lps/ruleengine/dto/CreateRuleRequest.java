package com.lps.ruleengine.dto;

import com.lps.ruleengine.model.Rule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new rule")
public class CreateRuleRequest {

    @NotBlank(message = "Rule ID is required")
    @Schema(description = "Unique identifier for the rule", example = "rule_age_check")
    private String ruleId;

    @NotBlank(message = "Expression is required")
    @Schema(description = "Rule expression", example = "age > 18")
    private String expression;

    @Schema(description = "Reference to document containing comparison value", example = "doc_min_age")
    private String referenceId;

    @NotNull(message = "OnTrue type is required")
    @Schema(description = "Type of action when condition is true")
    private Rule.OutcomeType onTrueType;

    @NotBlank(message = "OnTrue value is required")
    @Schema(description = "Value or rule ID when condition is true", example = "true")
    private String onTrueValue;

    @NotNull(message = "OnFalse type is required")
    @Schema(description = "Type of action when condition is false")
    private Rule.OutcomeType onFalseType;

    @NotBlank(message = "OnFalse value is required")
    @Schema(description = "Value or rule ID when condition is false", example = "false")
    private String onFalseValue;

    @Schema(description = "Optional description of the rule")
    private String description;
}
