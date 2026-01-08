package com.lps.ruleengine.adaptor;

import com.lps.ruleengine.dto.CreateRuleRequest;
import com.lps.ruleengine.model.Rule;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptor class for Rule entity builder patterns.
 * Encapsulates all Rule builder logic in one place.
 */
@Component
public class RuleAdaptor {

    /**
     * Creates a Rule from CreateRuleRequest using builder pattern.
     *
     * @param request the create rule request
     * @return Rule entity built from request
     */
    public Rule createRuleFromRequest(CreateRuleRequest request) {
        return Rule.builder()
                .ruleId(request.getRuleId())
                .expression(request.getExpression())
                .referenceId(request.getReferenceId())
                .onTrueType(request.getOnTrueType())
                .onTrueValue(request.getOnTrueValue())
                .onFalseType(request.getOnFalseType())
                .onFalseValue(request.getOnFalseValue())
                .description(request.getDescription())
                .build();
    }

    /**
     * Creates a Rule for system initialization with all parameters.
     *
     * @param ruleId the rule ID
     * @param expression the rule expression
     * @param referenceId the reference ID for lookup values
     * @param onTrueType the outcome type when rule evaluates to true
     * @param onTrueValue the outcome value when rule evaluates to true
     * @param onFalseType the outcome type when rule evaluates to false
     * @param onFalseValue the outcome value when rule evaluates to false
     * @param description the rule description
     * @param isActive whether the rule is active
     * @return Rule entity built from parameters
     */
    public Rule createRule(String ruleId, String expression, String referenceId, 
                          Rule.OutcomeType onTrueType, String onTrueValue,
                          Rule.OutcomeType onFalseType, String onFalseValue,
                          String description, Boolean isActive) {
        return Rule.builder()
                .ruleId(ruleId)
                .expression(expression)
                .referenceId(referenceId)
                .onTrueType(onTrueType)
                .onTrueValue(onTrueValue)
                .onFalseType(onFalseType)
                .onFalseValue(onFalseValue)
                .description(description)
                .isActive(isActive)
                .build();
    }

    /**
     * Creates a simple Rule with basic parameters.
     *
     * @param ruleId the rule ID
     * @param expression the rule expression
     * @param description the rule description
     * @param onTrueValue the outcome value when rule evaluates to true
     * @param onFalseValue the outcome value when rule evaluates to false
     * @return Rule entity built from parameters
     */
    public Rule createSimpleRule(String ruleId, String expression, String description,
                                String onTrueValue, String onFalseValue) {
        return Rule.builder()
                .ruleId(ruleId)
                .expression(expression)
                .description(description)
                .onTrueType(Rule.OutcomeType.VALUE)
                .onTrueValue(onTrueValue)
                .onFalseType(Rule.OutcomeType.VALUE)
                .onFalseValue(onFalseValue)
                .isActive(true)
                .build();
    }
}
