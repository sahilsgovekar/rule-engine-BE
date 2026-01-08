package com.lps.ruleengine.controller;

import com.lps.ruleengine.dto.CreateRuleRequest;
import com.lps.ruleengine.dto.EvaluationRequest;
import com.lps.ruleengine.dto.EvaluationResponse;
import com.lps.ruleengine.model.Rule;
import com.lps.ruleengine.service.IRuleEvaluationService;
import com.lps.ruleengine.service.IRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rule Management", description = "APIs for managing and evaluating rules")
public class RuleController {

    private final IRuleService ruleService;
    private final IRuleEvaluationService ruleEvaluationService;

    @Operation(summary = "Create a new rule", description = "Creates a new rule with the specified parameters")
    @PostMapping
    public ResponseEntity<Rule> createRule(@Valid @RequestBody CreateRuleRequest request) {
        log.info("Creating rule: {}", request.getRuleId());
        try {
            Rule rule = ruleService.createRule(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(rule);
        } catch (Exception e) {
            log.error("Error creating rule: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get all rules", description = "Retrieves all rules in the system")
    @GetMapping
    public ResponseEntity<List<Rule>> getAllRules() {
        List<Rule> rules = ruleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @Operation(summary = "Get active rules only", description = "Retrieves only active rules")
    @GetMapping("/active")
    public ResponseEntity<List<Rule>> getActiveRules() {
        List<Rule> rules = ruleService.getActiveRules();
        return ResponseEntity.ok(rules);
    }

    @Operation(summary = "Get rule by ID", description = "Retrieves a specific rule by its ID")
    @GetMapping("/{ruleId}")
    public ResponseEntity<Rule> getRuleById(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {
        Optional<Rule> rule = ruleService.getRuleById(ruleId);
        return rule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing rule", description = "Updates an existing rule with new parameters")
    @PutMapping("/{ruleId}")
    public ResponseEntity<Rule> updateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId,
            @Valid @RequestBody CreateRuleRequest request) {
        try {
            Rule updatedRule = ruleService.updateRule(ruleId, request);
            return ResponseEntity.ok(updatedRule);
        } catch (Exception e) {
            log.error("Error updating rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a rule", description = "Deletes a rule from the system")
    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {
        try {
            ruleService.deleteRule(ruleId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Activate a rule", description = "Sets a rule as active")
    @PatchMapping("/{ruleId}/activate")
    public ResponseEntity<Void> activateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {
        try {
            ruleService.activateRule(ruleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error activating rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Deactivate a rule", description = "Sets a rule as inactive")
    @PatchMapping("/{ruleId}/deactivate")
    public ResponseEntity<Void> deactivateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {
        try {
            ruleService.deactivateRule(ruleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deactivating rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Evaluate a rule", description = "Evaluates a rule against user attributes")
    @PostMapping("/{ruleId}/evaluate")
    public ResponseEntity<EvaluationResponse> evaluateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId,
            @Valid @RequestBody EvaluationRequest request) {
        log.info("Evaluating rule: {} for user: {}", ruleId, request.getUserId());
        try {
            EvaluationResponse response = ruleEvaluationService.evaluateRule(
                    ruleId, request.getUserId(), request.getUserAttributes());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error evaluating rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Search rules by expression", description = "Searches for rules containing a keyword in their expression")
    @GetMapping("/search")
    public ResponseEntity<List<Rule>> searchRules(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<Rule> rules = ruleService.searchRulesByExpression(keyword);
        return ResponseEntity.ok(rules);
    }

    @Operation(summary = "Get active rule count", description = "Returns the count of active rules")
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveRuleCount() {
        long count = ruleService.getActiveRuleCount();
        return ResponseEntity.ok(count);
    }
}
