package com.lps.ruleengine.service.impl;

import com.lps.ruleengine.dto.EvaluationResponse;
import com.lps.ruleengine.model.Policy;
import com.lps.ruleengine.repository.PolicyRepository;
import com.lps.ruleengine.service.IPolicyEvaluationService;
import com.lps.ruleengine.service.IRuleEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEvaluationService implements IPolicyEvaluationService {

    private final PolicyRepository policyRepository;
    private final IRuleEvaluationService ruleEvaluationService;

    /**
     * Evaluates a policy for a user with given attributes
     * This is the primary client-facing evaluation method
     */
    @Override
    public EvaluationResponse evaluatePolicy(String policyId, String userId, Map<String, Object> userAttributes) {
        log.info("Evaluating policy: {} for user: {}", policyId, userId);
        
        // Validate policy exists and is active
        Optional<Policy> policyOpt = policyRepository.findByPolicyId(policyId);
        if (policyOpt.isEmpty()) {
            throw new RuntimeException("Policy not found: " + policyId);
        }
        
        Policy policy = policyOpt.get();
        if (!policy.getIsActive()) {
            throw new RuntimeException("Policy is inactive: " + policyId);
        }
        
        log.debug("Policy found: {}, root rule: {}", policy.getPolicyName(), policy.getRootRuleId());
        
        // Evaluate starting from the root rule
        EvaluationResponse response = ruleEvaluationService.evaluateRule(
            policy.getRootRuleId(), userId, userAttributes);
        
        // Update response to indicate it was a policy evaluation
        response.setEvaluatedId(policyId);
        response.setEvaluationType("POLICY");
        
        log.info("Policy evaluation completed for user: {}, result: {}", userId, response.getResult());
        
        return response;
    }

    /**
     * Evaluates a single rule (for client applications that need direct rule evaluation)
     */
    @Override
    public EvaluationResponse evaluateRule(String ruleId, String userId, Map<String, Object> userAttributes) {
        log.info("Direct rule evaluation: {} for user: {}", ruleId, userId);
        
        return ruleEvaluationService.evaluateRule(ruleId, userId, userAttributes);
    }

    /**
     * Bulk policy evaluation for multiple policies
     * Useful for comparing different policy outcomes
     */
    @Override
    public Map<String, EvaluationResponse> evaluateMultiplePolicies(
            String[] policyIds, String userId, Map<String, Object> userAttributes) {
        
        log.info("Bulk evaluation for user: {} across {} policies", userId, policyIds.length);
        
        return java.util.Arrays.stream(policyIds)
            .collect(java.util.stream.Collectors.toMap(
                policyId -> policyId,
                policyId -> evaluatePolicy(policyId, userId, userAttributes)
            ));
    }
}
