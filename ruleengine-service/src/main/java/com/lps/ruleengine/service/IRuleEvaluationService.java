package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.EvaluationResponse;

import java.util.Map;

/**
 * Interface for rule evaluation operations.
 * Defines contract for evaluating individual rules against user attributes.
 */
public interface IRuleEvaluationService {

    /**
     * Evaluates a rule against user attributes
     * @param ruleId The rule identifier to evaluate
     * @param userId The user identifier
     * @param userAttributes The user attributes to evaluate the rule against
     * @return The evaluation response containing result and execution trace
     * @throws RuntimeException if rule not found or inactive
     */
    EvaluationResponse evaluateRule(String ruleId, String userId, Map<String, Object> userAttributes);
}
