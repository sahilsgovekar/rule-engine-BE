package com.lps.ruleengine.config;

import com.lps.ruleengine.model.Document;
import com.lps.ruleengine.model.Policy;
import com.lps.ruleengine.model.Rule;
import com.lps.ruleengine.repository.DocumentRepository;
import com.lps.ruleengine.repository.PolicyRepository;
import com.lps.ruleengine.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DocumentRepository documentRepository;
    private final RuleRepository ruleRepository;
    private final PolicyRepository policyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (documentRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeDocuments();
            initializeRules();
            initializePolicies();
            log.info("Sample data initialization completed");
        }
    }

    private void initializeDocuments() {
        List<Document> documents = Arrays.asList(
            Document.of("doc_min_age", 18),
            Document.of("doc_max_age", 65),
            Document.of("doc_min_income", 25000),
            Document.of("doc_allowed_cities", Arrays.asList("Bangalore", "Mumbai", "Delhi", "Chennai")),
            Document.of("doc_high_risk_amount", 500000),
            Document.of("doc_true", true),
            Document.of("doc_false", false)
        );

        documentRepository.saveAll(documents);
        log.info("Created {} sample documents", documents.size());
    }

    private void initializeRules() {
        List<Rule> rules = Arrays.asList(
            // Age check rule
            Rule.builder()
                .ruleId("rule_age_check")
                .expression("age >= 18")
                .referenceId("doc_min_age")
                .onTrueType(Rule.OutcomeType.RULE)
                .onTrueValue("rule_city_check")
                .onFalseType(Rule.OutcomeType.VALUE)
                .onFalseValue("false")
                .description("Check if user is at least 18 years old")
                .build(),

            // City check rule
            Rule.builder()
                .ruleId("rule_city_check")
                .expression("city IN allowedCities")
                .referenceId("doc_allowed_cities")
                .onTrueType(Rule.OutcomeType.RULE)
                .onTrueValue("rule_income_check")
                .onFalseType(Rule.OutcomeType.VALUE)
                .onFalseValue("false")
                .description("Check if user is from allowed cities")
                .build(),

            // Income check rule
            Rule.builder()
                .ruleId("rule_income_check")
                .expression("income >= 25000")
                .referenceId("doc_min_income")
                .onTrueType(Rule.OutcomeType.RULE)
                .onTrueValue("rule_amount_check")
                .onFalseType(Rule.OutcomeType.VALUE)
                .onFalseValue("false")
                .description("Check if user has minimum required income")
                .build(),

            // Amount check rule
            Rule.builder()
                .ruleId("rule_amount_check")
                .expression("loanAmount < 500000")
                .referenceId("doc_high_risk_amount")
                .onTrueType(Rule.OutcomeType.VALUE)
                .onTrueValue("true")
                .onFalseType(Rule.OutcomeType.RULE)
                .onFalseValue("rule_high_amount_check")
                .description("Check loan amount threshold")
                .build(),

            // High amount additional check
            Rule.builder()
                .ruleId("rule_high_amount_check")
                .expression("age <= 65")
                .referenceId("doc_max_age")
                .onTrueType(Rule.OutcomeType.VALUE)
                .onTrueValue("true")
                .onFalseType(Rule.OutcomeType.VALUE)
                .onFalseValue("false")
                .description("Additional check for high amount loans")
                .build(),

            // Simple approve rule
            Rule.builder()
                .ruleId("rule_simple_approve")
                .expression("age >= 18")
                .referenceId("doc_min_age")
                .onTrueType(Rule.OutcomeType.VALUE)
                .onTrueValue("true")
                .onFalseType(Rule.OutcomeType.VALUE)
                .onFalseValue("false")
                .description("Simple age-based approval rule")
                .build()
        );

        ruleRepository.saveAll(rules);
        log.info("Created {} sample rules", rules.size());
    }

    private void initializePolicies() {
        List<Policy> policies = Arrays.asList(
            Policy.builder()
                .policyId("policy_standard_loan")
                .policyName("Standard Loan Approval Policy")
                .description("Standard policy for regular loan applications")
                .rootRuleId("rule_age_check")
                .ruleIds(new HashSet<>(Arrays.asList("rule_age_check", "rule_city_check", 
                                                   "rule_income_check", "rule_amount_check", 
                                                   "rule_high_amount_check")))
                .priority(1)
                .build(),

            Policy.builder()
                .policyId("policy_simple_loan")
                .policyName("Simple Loan Approval Policy")
                .description("Simplified policy with only age check")
                .rootRuleId("rule_simple_approve")
                .ruleIds(new HashSet<>(Arrays.asList("rule_simple_approve")))
                .priority(2)
                .build()
        );

        policyRepository.saveAll(policies);
        log.info("Created {} sample policies", policies.size());
    }
}
