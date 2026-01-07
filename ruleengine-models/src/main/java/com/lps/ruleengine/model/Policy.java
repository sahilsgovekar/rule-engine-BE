package com.lps.ruleengine.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Policy {

    @Id
    @Column(name = "policy_id")
    private String policyId;

    @Column(name = "policy_name", nullable = false)
    private String policyName;

    @Column(name = "description")
    private String description;

    @Column(name = "root_rule_id", nullable = false)
    private String rootRuleId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "policy_rules",
        joinColumns = @JoinColumn(name = "policy_id")
    )
    @Column(name = "rule_id")
    private Set<String> ruleIds;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "priority")
    @Builder.Default
    private Integer priority = 1;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @Builder.Default
    private Integer version = 1;

    // Business helper methods
    public boolean containsRule(String ruleId) {
        return ruleIds != null && ruleIds.contains(ruleId);
    }

    public void addRule(String ruleId) {
        if (ruleIds != null) {
            ruleIds.add(ruleId);
        }
    }

    public void removeRule(String ruleId) {
        if (ruleIds != null) {
            ruleIds.remove(ruleId);
        }
    }
}
