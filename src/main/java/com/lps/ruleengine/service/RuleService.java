package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.CreateRuleRequest;
import com.lps.ruleengine.model.Rule;
import com.lps.ruleengine.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService {

    private final RuleRepository ruleRepository;

    public Rule createRule(CreateRuleRequest request) {
        log.debug("Creating rule: {}", request.getRuleId());
        
        if (ruleRepository.existsByRuleId(request.getRuleId())) {
            throw new RuntimeException("Rule already exists: " + request.getRuleId());
        }
        
        Rule rule = Rule.builder()
                .ruleId(request.getRuleId())
                .expression(request.getExpression())
                .referenceId(request.getReferenceId())
                .onTrueType(request.getOnTrueType())
                .onTrueValue(request.getOnTrueValue())
                .onFalseType(request.getOnFalseType())
                .onFalseValue(request.getOnFalseValue())
                .description(request.getDescription())
                .build();
        
        return ruleRepository.save(rule);
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public List<Rule> getActiveRules() {
        return ruleRepository.findByIsActiveTrue();
    }

    public Optional<Rule> getRuleById(String ruleId) {
        return ruleRepository.findByRuleId(ruleId);
    }

    public Rule updateRule(String ruleId, CreateRuleRequest request) {
        Optional<Rule> existingOpt = ruleRepository.findByRuleId(ruleId);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Rule not found: " + ruleId);
        }
        
        Rule existing = existingOpt.get();
        existing.setExpression(request.getExpression());
        existing.setReferenceId(request.getReferenceId());
        existing.setOnTrueType(request.getOnTrueType());
        existing.setOnTrueValue(request.getOnTrueValue());
        existing.setOnFalseType(request.getOnFalseType());
        existing.setOnFalseValue(request.getOnFalseValue());
        existing.setDescription(request.getDescription());
        existing.setVersion(existing.getVersion() + 1);
        
        return ruleRepository.save(existing);
    }

    public void deleteRule(String ruleId) {
        if (!ruleRepository.existsByRuleId(ruleId)) {
            throw new RuntimeException("Rule not found: " + ruleId);
        }
        ruleRepository.deleteById(ruleId);
    }

    public void activateRule(String ruleId) {
        updateRuleStatus(ruleId, true);
    }

    public void deactivateRule(String ruleId) {
        updateRuleStatus(ruleId, false);
    }

    private void updateRuleStatus(String ruleId, boolean isActive) {
        Optional<Rule> ruleOpt = ruleRepository.findByRuleId(ruleId);
        if (ruleOpt.isEmpty()) {
            throw new RuntimeException("Rule not found: " + ruleId);
        }
        
        Rule rule = ruleOpt.get();
        rule.setIsActive(isActive);
        rule.setVersion(rule.getVersion() + 1);
        ruleRepository.save(rule);
    }

    public List<Rule> searchRulesByExpression(String keyword) {
        return ruleRepository.findByExpressionContaining(keyword);
    }

    public long getActiveRuleCount() {
        return ruleRepository.countActiveRules();
    }
}
