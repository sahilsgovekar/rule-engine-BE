package com.lps.ruleengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new policy")
public class CreatePolicyRequest {

    @NotBlank(message = "Policy ID is required")
    @Schema(description = "Unique identifier for the policy", example = "policy_loan_approval")
    private String policyId;

    @NotBlank(message = "Policy name is required")
    @Schema(description = "Human readable name for the policy", example = "Loan Approval Policy")
    private String policyName;

    @Schema(description = "Optional description of the policy")
    private String description;

    @NotBlank(message = "Root rule ID is required")
    @Schema(description = "ID of the starting rule for this policy", example = "rule_age_check")
    private String rootRuleId;

    @NotEmpty(message = "Rule IDs cannot be empty")
    @Schema(description = "Set of all rule IDs that belong to this policy")
    private Set<String> ruleIds;

    @Schema(description = "Priority of the policy (higher number = higher priority)", example = "1")
    private Integer priority;
}
