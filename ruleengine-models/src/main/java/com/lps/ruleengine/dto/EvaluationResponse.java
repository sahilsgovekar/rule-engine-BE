package com.lps.ruleengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for rule or policy evaluation")
public class EvaluationResponse {

    @Schema(description = "Evaluation result - true for eligible, false for not eligible")
    private Boolean result;

    @Schema(description = "User ID for which evaluation was performed")
    private String userId;

    @Schema(description = "Rule or Policy ID that was evaluated")
    private String evaluatedId;

    @Schema(description = "Type of evaluation - RULE or POLICY")
    private String evaluationType;

    @Schema(description = "Evaluation trace showing which rules were executed")
    private List<ExecutionTrace> executionTrace;

    @Schema(description = "Time when evaluation was performed")
    private LocalDateTime evaluatedAt;

    @Schema(description = "Any error message if evaluation failed")
    private String errorMessage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutionTrace {
        private String ruleId;
        private String expression;
        private Boolean evaluationResult;
        private String nextAction;
    }
}
