package com.lps.ruleengine.config;

import com.lps.ruleengine.adaptor.DocumentAdaptor;
import com.lps.ruleengine.adaptor.PolicyAdaptor;
import com.lps.ruleengine.adaptor.RuleAdaptor;
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
    private final DocumentAdaptor documentAdaptor;
    private final RuleAdaptor ruleAdaptor;
    private final PolicyAdaptor policyAdaptor;

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
            documentAdaptor.createDocumentFromIdAndValue("doc_min_age", 18),
            documentAdaptor.createDocumentFromIdAndValue("doc_max_age", 65),
            documentAdaptor.createDocumentFromIdAndValue("doc_min_income", 25000),
            documentAdaptor.createDocumentFromIdAndValue("doc_allowed_cities", Arrays.asList("Bangalore", "Mumbai", "Delhi", "Chennai")),
            documentAdaptor.createDocumentFromIdAndValue("doc_high_risk_amount", 500000),
            documentAdaptor.createDocumentFromIdAndValue("doc_true", true),
            documentAdaptor.createDocumentFromIdAndValue("doc_false", false)
        );

        documentRepository.saveAll(documents);
        log.info("Created {} sample documents", documents.size());
    }

    private void initializeRules() {
        List<Rule> rules = Arrays.asList(
            // Age check rule
            ruleAdaptor.createRule("rule_age_check", "age >= 18", "doc_min_age",
                    Rule.OutcomeType.RULE, "rule_city_check",
                    Rule.OutcomeType.VALUE, "false",
                    "Check if user is at least 18 years old", true),

            // City check rule
            ruleAdaptor.createRule("rule_city_check", "city IN allowedCities", "doc_allowed_cities",
                    Rule.OutcomeType.RULE, "rule_income_check",
                    Rule.OutcomeType.VALUE, "false",
                    "Check if user is from allowed cities", true),

            // Income check rule
            ruleAdaptor.createRule("rule_income_check", "income >= 25000", "doc_min_income",
                    Rule.OutcomeType.RULE, "rule_amount_check",
                    Rule.OutcomeType.VALUE, "false",
                    "Check if user has minimum required income", true),

            // Amount check rule
            ruleAdaptor.createRule("rule_amount_check", "loanAmount < 500000", "doc_high_risk_amount",
                    Rule.OutcomeType.VALUE, "true",
                    Rule.OutcomeType.RULE, "rule_high_amount_check",
                    "Check loan amount threshold", true),

            // High amount additional check
            ruleAdaptor.createRule("rule_high_amount_check", "age <= 65", "doc_max_age",
                    Rule.OutcomeType.VALUE, "true",
                    Rule.OutcomeType.VALUE, "false",
                    "Additional check for high amount loans", true),

            // Simple approve rule
            ruleAdaptor.createSimpleRule("rule_simple_approve", "age >= 18", 
                    "Simple age-based approval rule", "true", "false")
        );

        ruleRepository.saveAll(rules);
        log.info("Created {} sample rules", rules.size());
    }

    private void initializePolicies() {
        List<Policy> policies = Arrays.asList(
            policyAdaptor.createPolicy("policy_standard_loan", "Standard Loan Approval Policy",
                    "Standard policy for regular loan applications", "rule_age_check",
                    Arrays.asList("rule_age_check", "rule_city_check", "rule_income_check", 
                                 "rule_amount_check", "rule_high_amount_check"), 
                    1, true),

            policyAdaptor.createPolicy("policy_simple_loan", "Simple Loan Approval Policy",
                    "Simplified policy with only age check", "rule_simple_approve",
                    Arrays.asList("rule_simple_approve"), 2, true)
        );

        policyRepository.saveAll(policies);
        log.info("Created {} sample policies", policies.size());
    }
}
