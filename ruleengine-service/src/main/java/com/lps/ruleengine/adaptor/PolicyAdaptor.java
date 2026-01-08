package com.lps.ruleengine.adaptor;

import com.lps.ruleengine.dto.CreatePolicyRequest;
import com.lps.ruleengine.model.Policy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * Adaptor class for Policy entity builder patterns.
 * Encapsulates all Policy builder logic in one place.
 */
@Component
public class PolicyAdaptor {

    /**
     * Creates a Policy from CreatePolicyRequest using builder pattern.
     *
     * @param request the create policy request
     * @return Policy entity built from request
     */
    public Policy createPolicyFromRequest(CreatePolicyRequest request) {
        return Policy.builder()
                .policyId(request.getPolicyId())
                .policyName(request.getPolicyName())
                .description(request.getDescription())
                .rootRuleId(request.getRootRuleId())
                .ruleIds(request.getRuleIds() != null ? new HashSet<>(request.getRuleIds()) : new HashSet<>())
                .priority(request.getPriority() != null ? request.getPriority() : 1)
                .build();
    }

    /**
     * Creates a Policy for system initialization with all parameters.
     *
     * @param policyId the policy ID
     * @param policyName the policy name
     * @param description the policy description
     * @param rootRuleId the root rule ID for policy evaluation
     * @param ruleIds the list of rule IDs associated with this policy
     * @param priority the policy priority
     * @param isActive whether the policy is active
     * @return Policy entity built from parameters
     */
    public Policy createPolicy(String policyId, String policyName, String description,
                              String rootRuleId, List<String> ruleIds, Integer priority,
                              Boolean isActive) {
        return Policy.builder()
                .policyId(policyId)
                .policyName(policyName)
                .description(description)
                .rootRuleId(rootRuleId)
                .ruleIds(new HashSet<>(ruleIds))
                .priority(priority != null ? priority : 1)
                .isActive(isActive != null ? isActive : true)
                .build();
    }

    /**
     * Creates a simple Policy with basic parameters.
     *
     * @param policyId the policy ID
     * @param policyName the policy name
     * @param description the policy description
     * @param rootRuleId the root rule ID for policy evaluation
     * @param ruleIds the list of rule IDs associated with this policy
     * @return Policy entity built from parameters
     */
    public Policy createSimplePolicy(String policyId, String policyName, String description,
                                   String rootRuleId, List<String> ruleIds) {
        return Policy.builder()
                .policyId(policyId)
                .policyName(policyName)
                .description(description)
                .rootRuleId(rootRuleId)
                .ruleIds(new HashSet<>(ruleIds))
                .priority(1)
                .isActive(true)
                .build();
    }
}
