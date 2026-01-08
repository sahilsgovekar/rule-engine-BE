package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.EvaluationResponse;

import java.util.Map;

/**
 * Interface for policy evaluation operations.
 * Defines contract for evaluating policies and rules against user attributes.
 */
public interface IPolicyEvaluationService {

    /**
     * Evaluates a policy for a user with given attributes
     * This is the primary client-facing evaluation method
     * @param policyId The policy identifier
     * @param userId The user identifier
     * @param userAttributes The user attributes to evaluate against
     * @return The evaluation response
     * @throws RuntimeException if policy not found or inactive
     */
    EvaluationResponse evaluatePolicy(String policyId, String userId, Map<String, Object> userAttributes);

    /**
     * Evaluates a single rule (for client applications that need direct rule evaluation)
     * @param ruleId The rule identifier
     * @param userId The user identifier
     * @param userAttributes The user attributes to evaluate against
     * @return The evaluation response
     */
    EvaluationResponse evaluateRule(String ruleId, String userId, Map<String, Object> userAttributes);

    /**
     * Bulk policy evaluation for multiple policies
     * Useful for comparing different policy outcomes
     * @param policyIds Array of policy identifiers
     * @param userId The user identifier
     * @param userAttributes The user attributes to evaluate against
     * @return Map of policy ID to evaluation response
     */
    Map<String, EvaluationResponse> evaluateMultiplePolicies(
            String[] policyIds, String userId, Map<String, Object> userAttributes);
}
