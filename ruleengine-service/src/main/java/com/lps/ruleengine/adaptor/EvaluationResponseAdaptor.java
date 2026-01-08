package com.lps.ruleengine.adaptor;

import com.lps.ruleengine.dto.EvaluationResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adaptor class for EvaluationResponse builder patterns.
 * Encapsulates all EvaluationResponse builder logic in one place.
 */
@Component
public class EvaluationResponseAdaptor {

    /**
     * Creates a successful EvaluationResponse.
     *
     * @param result the evaluation result
     * @param userId the user ID
     * @param evaluatedId the evaluated entity ID (rule/policy)
     * @param evaluationType the type of evaluation (RULE/POLICY)
     * @param executionTrace the execution trace
     * @return EvaluationResponse entity built from parameters
     */
    public EvaluationResponse createSuccessResponse(boolean result, String userId, String evaluatedId,
                                                   String evaluationType, List<EvaluationResponse.ExecutionTrace> executionTrace) {
        return EvaluationResponse.builder()
                .result(result)
                .userId(userId)
                .evaluatedId(evaluatedId)
                .evaluationType(evaluationType)
                .executionTrace(executionTrace)
                .evaluatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an error EvaluationResponse.
     *
     * @param userId the user ID
     * @param evaluatedId the evaluated entity ID (rule/policy)
     * @param evaluationType the type of evaluation (RULE/POLICY)
     * @param executionTrace the execution trace
     * @param errorMessage the error message
     * @return EvaluationResponse entity built from parameters
     */
    public EvaluationResponse createErrorResponse(String userId, String evaluatedId,
                                                 String evaluationType, List<EvaluationResponse.ExecutionTrace> executionTrace,
                                                 String errorMessage) {
        return EvaluationResponse.builder()
                .result(false)
                .userId(userId)
                .evaluatedId(evaluatedId)
                .evaluationType(evaluationType)
                .executionTrace(executionTrace)
                .evaluatedAt(LocalDateTime.now())
                .errorMessage(errorMessage)
                .build();
    }

    /**
     * Creates an ExecutionTrace entry.
     *
     * @param ruleId the rule ID
     * @param expression the rule expression
     * @param evaluationResult the evaluation result
     * @param nextAction the next action description
     * @return ExecutionTrace entity built from parameters
     */
    public EvaluationResponse.ExecutionTrace createExecutionTrace(String ruleId, String expression,
                                                                 boolean evaluationResult, String nextAction) {
        return EvaluationResponse.ExecutionTrace.builder()
                .ruleId(ruleId)
                .expression(expression)
                .evaluationResult(evaluationResult)
                .nextAction(nextAction)
                .build();
    }
}
