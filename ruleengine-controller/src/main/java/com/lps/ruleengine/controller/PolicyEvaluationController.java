package com.lps.ruleengine.controller;

import com.lps.ruleengine.dto.EvaluationRequest;
import com.lps.ruleengine.dto.EvaluationResponse;
import com.lps.ruleengine.service.IPolicyEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Policy & Rule Evaluation", description = "APIs for evaluating policies and rules against user data")
public class PolicyEvaluationController {

    private final IPolicyEvaluationService policyEvaluationService;

    @Operation(
        summary = "Evaluate a policy", 
        description = "Evaluates a policy against user attributes to determine loan eligibility. " +
                     "This is the primary endpoint used by client applications for loan decisions."
    )
    @PostMapping("/policies/{policyId}")
    public ResponseEntity<EvaluationResponse> evaluatePolicy(
            @Parameter(description = "Policy ID to evaluate") @PathVariable String policyId,
            @Valid @RequestBody EvaluationRequest request) {
        
        log.info("Policy evaluation request for: {} by user: {}", policyId, request.getUserId());
        
        try {
            EvaluationResponse response = policyEvaluationService.evaluatePolicy(
                    policyId, request.getUserId(), request.getUserAttributes());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error evaluating policy {}: {}", policyId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Evaluate a single rule", 
        description = "Directly evaluates a single rule against user attributes. " +
                     "Useful for testing individual rules or lightweight evaluations."
    )
    @PostMapping("/rules/{ruleId}")
    public ResponseEntity<EvaluationResponse> evaluateRule(
            @Parameter(description = "Rule ID to evaluate") @PathVariable String ruleId,
            @Valid @RequestBody EvaluationRequest request) {
        
        log.info("Rule evaluation request for: {} by user: {}", ruleId, request.getUserId());
        
        try {
            EvaluationResponse response = policyEvaluationService.evaluateRule(
                    ruleId, request.getUserId(), request.getUserAttributes());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error evaluating rule {}: {}", ruleId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Bulk policy evaluation", 
        description = "Evaluates multiple policies for the same user to compare outcomes. " +
                     "Useful for A/B testing or policy comparison scenarios."
    )
    @PostMapping("/policies/bulk")
    public ResponseEntity<Map<String, EvaluationResponse>> evaluateMultiplePolicies(
            @Parameter(description = "Array of policy IDs to evaluate") @RequestParam String[] policyIds,
            @Valid @RequestBody EvaluationRequest request) {
        
        log.info("Bulk evaluation request for user: {} across {} policies", 
                request.getUserId(), policyIds.length);
        
        try {
            Map<String, EvaluationResponse> responses = policyEvaluationService.evaluateMultiplePolicies(
                    policyIds, request.getUserId(), request.getUserAttributes());
            
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error in bulk evaluation for user {}: {}", request.getUserId(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
