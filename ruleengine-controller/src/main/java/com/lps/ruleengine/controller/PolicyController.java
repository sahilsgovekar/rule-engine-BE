package com.lps.ruleengine.controller;

import com.lps.ruleengine.dto.CreatePolicyRequest;
import com.lps.ruleengine.model.Policy;
import com.lps.ruleengine.service.IPolicyService;
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
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Policy Management", description = "APIs for managing and evaluating policies")
public class PolicyController {

    private final IPolicyService policyService;

    @Operation(summary = "Create a new policy", description = "Creates a new policy with the specified rules")
    @PostMapping
    public ResponseEntity<Policy> createPolicy(@Valid @RequestBody CreatePolicyRequest request) {
        log.info("Creating policy: {}", request.getPolicyId());
        try {
            Policy policy = policyService.createPolicy(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(policy);
        } catch (Exception e) {
            log.error("Error creating policy: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get all policies", description = "Retrieves all policies in the system")
    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        List<Policy> policies = policyService.getAllPolicies();
        return ResponseEntity.ok(policies);
    }

    @Operation(summary = "Get active policies only", description = "Retrieves only active policies")
    @GetMapping("/active")
    public ResponseEntity<List<Policy>> getActivePolicies() {
        List<Policy> policies = policyService.getActivePolicies();
        return ResponseEntity.ok(policies);
    }

    @Operation(summary = "Get policy by ID", description = "Retrieves a specific policy by its ID")
    @GetMapping("/{policyId}")
    public ResponseEntity<Policy> getPolicyById(
            @Parameter(description = "Policy ID") @PathVariable String policyId) {
        Optional<Policy> policy = policyService.getPolicyById(policyId);
        return policy.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing policy", description = "Updates an existing policy with new parameters")
    @PutMapping("/{policyId}")
    public ResponseEntity<Policy> updatePolicy(
            @Parameter(description = "Policy ID") @PathVariable String policyId,
            @Valid @RequestBody CreatePolicyRequest request) {
        try {
            Policy updatedPolicy = policyService.updatePolicy(policyId, request);
            return ResponseEntity.ok(updatedPolicy);
        } catch (Exception e) {
            log.error("Error updating policy: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a policy", description = "Deletes a policy from the system")
    @DeleteMapping("/{policyId}")
    public ResponseEntity<Void> deletePolicy(
            @Parameter(description = "Policy ID") @PathVariable String policyId) {
        try {
            policyService.deletePolicy(policyId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting policy: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Activate a policy", description = "Sets a policy as active")
    @PatchMapping("/{policyId}/activate")
    public ResponseEntity<Void> activatePolicy(
            @Parameter(description = "Policy ID") @PathVariable String policyId) {
        try {
            policyService.activatePolicy(policyId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error activating policy: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Deactivate a policy", description = "Sets a policy as inactive")
    @PatchMapping("/{policyId}/deactivate")
    public ResponseEntity<Void> deactivatePolicy(
            @Parameter(description = "Policy ID") @PathVariable String policyId) {
        try {
            policyService.deactivatePolicy(policyId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deactivating policy: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
