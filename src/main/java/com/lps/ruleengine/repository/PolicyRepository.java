package com.lps.ruleengine.repository;

import com.lps.ruleengine.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, String> {

    Optional<Policy> findByPolicyId(String policyId);

    List<Policy> findByIsActiveTrue();

    Optional<Policy> findByPolicyName(String policyName);

    List<Policy> findByRootRuleId(String rootRuleId);

    @Query("SELECT p FROM Policy p JOIN p.ruleIds r WHERE r = :ruleId")
    List<Policy> findPoliciesContainingRule(@Param("ruleId") String ruleId);

    @Query("SELECT p FROM Policy p WHERE p.priority = :priority AND p.isActive = true")
    List<Policy> findActivePoliciesByPriority(@Param("priority") Integer priority);

    boolean existsByPolicyId(String policyId);

    boolean existsByPolicyName(String policyName);

    @Query("SELECT COUNT(p) FROM Policy p WHERE p.isActive = true")
    long countActivePolicies();

    List<Policy> findByOrderByPriorityDesc();
}
