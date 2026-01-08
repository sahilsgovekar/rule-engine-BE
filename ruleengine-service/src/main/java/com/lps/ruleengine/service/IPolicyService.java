package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.CreatePolicyRequest;
import com.lps.ruleengine.model.Policy;

import java.util.List;
import java.util.Optional;

/**
 * Interface for policy management operations.
 * Defines contract for CRUD operations and policy-specific functionality.
 */
public interface IPolicyService {

    /**
     * Creates a new policy
     * @param request The policy creation request
     * @return The created policy
     * @throws RuntimeException if policy already exists
     */
    Policy createPolicy(CreatePolicyRequest request);

    /**
     * Retrieves all policies
     * @return List of all policies
     */
    List<Policy> getAllPolicies();

    /**
     * Retrieves all active policies
     * @return List of active policies
     */
    List<Policy> getActivePolicies();

    /**
     * Retrieves a policy by ID
     * @param policyId The policy identifier
     * @return Optional containing the policy if found
     */
    Optional<Policy> getPolicyById(String policyId);

    /**
     * Updates an existing policy
     * @param policyId The policy identifier
     * @param request The update request
     * @return The updated policy
     * @throws RuntimeException if policy not found
     */
    Policy updatePolicy(String policyId, CreatePolicyRequest request);

    /**
     * Deletes a policy
     * @param policyId The policy identifier
     * @throws RuntimeException if policy not found
     */
    void deletePolicy(String policyId);

    /**
     * Activates a policy
     * @param policyId The policy identifier
     * @throws RuntimeException if policy not found
     */
    void activatePolicy(String policyId);

    /**
     * Deactivates a policy
     * @param policyId The policy identifier
     * @throws RuntimeException if policy not found
     */
    void deactivatePolicy(String policyId);
}
