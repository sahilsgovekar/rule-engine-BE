package com.lps.ruleengine.service.impl;

import com.lps.ruleengine.adaptor.RuleAdaptor;
import com.lps.ruleengine.dto.CreateRuleRequest;
import com.lps.ruleengine.model.Rule;
import com.lps.ruleengine.repository.RuleRepository;
import com.lps.ruleengine.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService implements IRuleService {

    private final RuleRepository ruleRepository;
    private final RuleAdaptor ruleAdaptor;

    @Override
    public Rule createRule(CreateRuleRequest request) {
        log.debug("Creating rule: {}", request.getRuleId());
        
        if (ruleRepository.existsByRuleId(request.getRuleId())) {
            throw new RuntimeException("Rule already exists: " + request.getRuleId());
        }
        
        Rule rule = ruleAdaptor.createRuleFromRequest(request);
        
        return ruleRepository.save(rule);
    }

    @Override
    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Override
    public List<Rule> getActiveRules() {
        return ruleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Rule> getRuleById(String ruleId) {
        return ruleRepository.findByRuleId(ruleId);
    }

    @Override
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

    @Override
    public void deleteRule(String ruleId) {
        if (!ruleRepository.existsByRuleId(ruleId)) {
            throw new RuntimeException("Rule not found: " + ruleId);
        }
        ruleRepository.deleteById(ruleId);
    }

    @Override
    public void activateRule(String ruleId) {
        updateRuleStatus(ruleId, true);
    }

    @Override
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

    @Override
    public List<Rule> searchRulesByExpression(String keyword) {
        return ruleRepository.findByExpressionContaining(keyword);
    }

    @Override
    public long getActiveRuleCount() {
        return ruleRepository.countActiveRules();
    }
}
