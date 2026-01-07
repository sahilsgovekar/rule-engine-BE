package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.CreatePolicyRequest;
import com.lps.ruleengine.model.Policy;
import com.lps.ruleengine.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyService {

    private final PolicyRepository policyRepository;

    public Policy createPolicy(CreatePolicyRequest request) {
        log.debug("Creating policy: {}", request.getPolicyId());
        
        if (policyRepository.existsByPolicyId(request.getPolicyId())) {
            throw new RuntimeException("Policy already exists: " + request.getPolicyId());
        }
        
        if (policyRepository.existsByPolicyName(request.getPolicyName())) {
            throw new RuntimeException("Policy name already exists: " + request.getPolicyName());
        }
        
        Policy policy = Policy.builder()
                .policyId(request.getPolicyId())
                .policyName(request.getPolicyName())
                .description(request.getDescription())
                .rootRuleId(request.getRootRuleId())
                .ruleIds(request.getRuleIds())
                .priority(request.getPriority() != null ? request.getPriority() : 1)
                .build();
        
        return policyRepository.save(policy);
    }


    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public List<Policy> getActivePolicies() {
        return policyRepository.findByIsActiveTrue();
    }

    public Optional<Policy> getPolicyById(String policyId) {
        return policyRepository.findByPolicyId(policyId);
    }

    public Policy updatePolicy(String policyId, CreatePolicyRequest request) {
        Optional<Policy> existingOpt = policyRepository.findByPolicyId(policyId);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Policy not found: " + policyId);
        }
        
        Policy existing = existingOpt.get();
        existing.setPolicyName(request.getPolicyName());
        existing.setDescription(request.getDescription());
        existing.setRootRuleId(request.getRootRuleId());
        existing.setRuleIds(request.getRuleIds());
        existing.setPriority(request.getPriority() != null ? request.getPriority() : existing.getPriority());
        existing.setVersion(existing.getVersion() + 1);
        
        return policyRepository.save(existing);
    }

    public void deletePolicy(String policyId) {
        if (!policyRepository.existsByPolicyId(policyId)) {
            throw new RuntimeException("Policy not found: " + policyId);
        }
        policyRepository.deleteById(policyId);
    }

    public void activatePolicy(String policyId) {
        updatePolicyStatus(policyId, true);
    }

    public void deactivatePolicy(String policyId) {
        updatePolicyStatus(policyId, false);
    }

    private void updatePolicyStatus(String policyId, boolean isActive) {
        Optional<Policy> policyOpt = policyRepository.findByPolicyId(policyId);
        if (policyOpt.isEmpty()) {
            throw new RuntimeException("Policy not found: " + policyId);
        }
        
        Policy policy = policyOpt.get();
        policy.setIsActive(isActive);
        policy.setVersion(policy.getVersion() + 1);
        policyRepository.save(policy);
    }
}
