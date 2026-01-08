package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.CreateRuleRequest;
import com.lps.ruleengine.model.Rule;

import java.util.List;
import java.util.Optional;

/**
 * Interface for rule management operations.
 * Defines contract for CRUD operations and rule-specific functionality.
 */
public interface IRuleService {

    /**
     * Creates a new rule
     * @param request The rule creation request
     * @return The created rule
     * @throws RuntimeException if rule already exists
     */
    Rule createRule(CreateRuleRequest request);

    /**
     * Retrieves all rules
     * @return List of all rules
     */
    List<Rule> getAllRules();

    /**
     * Retrieves all active rules
     * @return List of active rules
     */
    List<Rule> getActiveRules();

    /**
     * Retrieves a rule by ID
     * @param ruleId The rule identifier
     * @return Optional containing the rule if found
     */
    Optional<Rule> getRuleById(String ruleId);

    /**
     * Updates an existing rule
     * @param ruleId The rule identifier
     * @param request The update request
     * @return The updated rule
     * @throws RuntimeException if rule not found
     */
    Rule updateRule(String ruleId, CreateRuleRequest request);

    /**
     * Deletes a rule
     * @param ruleId The rule identifier
     * @throws RuntimeException if rule not found
     */
    void deleteRule(String ruleId);

    /**
     * Activates a rule
     * @param ruleId The rule identifier
     * @throws RuntimeException if rule not found
     */
    void activateRule(String ruleId);

    /**
     * Deactivates a rule
     * @param ruleId The rule identifier
     * @throws RuntimeException if rule not found
     */
    void deactivateRule(String ruleId);

    /**
     * Searches rules by expression containing keyword
     * @param keyword The search keyword
     * @return List of matching rules
     */
    List<Rule> searchRulesByExpression(String keyword);

    /**
     * Gets count of active rules
     * @return Count of active rules
     */
    long getActiveRuleCount();
}
