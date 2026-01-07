package com.lps.ruleengine.repository;

import com.lps.ruleengine.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, String> {

    Optional<Rule> findByRuleId(String ruleId);

    List<Rule> findByIsActiveTrue();

    List<Rule> findByReferenceId(String referenceId);

    @Query("SELECT r FROM Rule r WHERE r.onTrueType = 'RULE' AND r.onTrueValue = :ruleId")
    List<Rule> findRulesWithOnTrueRuleId(@Param("ruleId") String ruleId);

    @Query("SELECT r FROM Rule r WHERE r.onFalseType = 'RULE' AND r.onFalseValue = :ruleId")
    List<Rule> findRulesWithOnFalseRuleId(@Param("ruleId") String ruleId);

    @Query("SELECT r FROM Rule r WHERE r.expression LIKE %:keyword%")
    List<Rule> findByExpressionContaining(@Param("keyword") String keyword);

    boolean existsByRuleId(String ruleId);

    @Query("SELECT COUNT(r) FROM Rule r WHERE r.isActive = true")
    long countActiveRules();
}
