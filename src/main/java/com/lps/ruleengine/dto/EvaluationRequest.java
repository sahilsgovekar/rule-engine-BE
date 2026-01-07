package com.lps.ruleengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for rule or policy evaluation")
public class EvaluationRequest {

    @NotBlank(message = "User ID is required")
    @Schema(description = "Unique identifier for the user", example = "user123")
    private String userId;

    @NotEmpty(message = "User attributes cannot be empty")
    @Schema(description = "User attributes for evaluation")
    private Map<String, Object> userAttributes;

    @Schema(description = "Optional context information")
    private Map<String, Object> context;
}
