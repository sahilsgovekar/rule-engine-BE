package com.lps.ruleengine.service;

import com.lps.ruleengine.dto.EvaluationResponse;
import com.lps.ruleengine.model.Document;
import com.lps.ruleengine.model.Rule;
import com.lps.ruleengine.repository.DocumentRepository;
import com.lps.ruleengine.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleEvaluationService {

    private final RuleRepository ruleRepository;
    private final DocumentRepository documentRepository;

    public EvaluationResponse evaluateRule(String ruleId, String userId, Map<String, Object> userAttributes) {
        log.debug("Starting rule evaluation for ruleId: {}, userId: {}", ruleId, userId);
        
        List<EvaluationResponse.ExecutionTrace> executionTrace = new ArrayList<>();
        
        try {
            boolean result = evaluateRuleRecursively(ruleId, userAttributes, executionTrace, new HashSet<>());
            
            return EvaluationResponse.builder()
                    .result(result)
                    .userId(userId)
                    .evaluatedId(ruleId)
                    .evaluationType("RULE")
                    .executionTrace(executionTrace)
                    .evaluatedAt(LocalDateTime.now())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error evaluating rule: {}", e.getMessage(), e);
            return EvaluationResponse.builder()
                    .result(false)
                    .userId(userId)
                    .evaluatedId(ruleId)
                    .evaluationType("RULE")
                    .executionTrace(executionTrace)
                    .evaluatedAt(LocalDateTime.now())
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    private boolean evaluateRuleRecursively(String ruleId, Map<String, Object> userAttributes, 
                                          List<EvaluationResponse.ExecutionTrace> trace, Set<String> visitedRules) {
        
        // Prevent infinite loops
        if (visitedRules.contains(ruleId)) {
            throw new RuntimeException("Circular dependency detected in rules: " + ruleId);
        }
        visitedRules.add(ruleId);
        
        Optional<Rule> ruleOpt = ruleRepository.findByRuleId(ruleId);
        if (ruleOpt.isEmpty()) {
            throw new RuntimeException("Rule not found: " + ruleId);
        }
        
        Rule rule = ruleOpt.get();
        if (!rule.getIsActive()) {
            throw new RuntimeException("Rule is inactive: " + ruleId);
        }
        
        // Evaluate the rule expression
        boolean expressionResult = evaluateExpression(rule.getExpression(), rule.getReferenceId(), userAttributes);
        
        // Add to execution trace
        trace.add(EvaluationResponse.ExecutionTrace.builder()
                .ruleId(ruleId)
                .expression(rule.getExpression())
                .evaluationResult(expressionResult)
                .nextAction(expressionResult ? "onTrue: " + rule.getOnTrueValue() : "onFalse: " + rule.getOnFalseValue())
                .build());
        
        // Determine next step based on result
        if (expressionResult) {
            if (rule.getOnTrueType() == Rule.OutcomeType.VALUE) {
                return rule.getOnTrueValueAsBoolean();
            } else {
                return evaluateRuleRecursively(rule.getOnTrueRuleId(), userAttributes, trace, new HashSet<>(visitedRules));
            }
        } else {
            if (rule.getOnFalseType() == Rule.OutcomeType.VALUE) {
                return rule.getOnFalseValueAsBoolean();
            } else {
                return evaluateRuleRecursively(rule.getOnFalseRuleId(), userAttributes, trace, new HashSet<>(visitedRules));
            }
        }
    }

    private boolean evaluateExpression(String expression, String referenceId, Map<String, Object> userAttributes) {
        log.debug("Evaluating expression: {}, referenceId: {}", expression, referenceId);
        
        // Get reference value if needed
        Object referenceValue = null;
        if (referenceId != null && !referenceId.isEmpty()) {
            Optional<Document> docOpt = documentRepository.findByDocumentId(referenceId);
            if (docOpt.isPresent()) {
                referenceValue = docOpt.get().getTypedValue();
            }
        }
        
        // Parse and evaluate expression
        return parseAndEvaluateExpression(expression, userAttributes, referenceValue);
    }

    private boolean parseAndEvaluateExpression(String expression, Map<String, Object> userAttributes, Object referenceValue) {
        // Simple expression parser for common operators
        String trimmedExpression = expression.trim();
        
        // Handle IN operator (e.g., "city IN ['Bangalore', 'Mumbai']")
        if (trimmedExpression.contains(" IN ")) {
            return evaluateInExpression(trimmedExpression, userAttributes, referenceValue);
        }
        
        // Handle comparison operators
        if (trimmedExpression.contains(" > ")) {
            return evaluateComparisonExpression(trimmedExpression, userAttributes, referenceValue, ">");
        }
        
        if (trimmedExpression.contains(" >= ")) {
            return evaluateComparisonExpression(trimmedExpression, userAttributes, referenceValue, ">=");
        }
        
        if (trimmedExpression.contains(" < ")) {
            return evaluateComparisonExpression(trimmedExpression, userAttributes, referenceValue, "<");
        }
        
        if (trimmedExpression.contains(" <= ")) {
            return evaluateComparisonExpression(trimmedExpression, userAttributes, referenceValue, "<=");
        }
        
        if (trimmedExpression.contains(" == ")) {
            return evaluateComparisonExpression(trimmedExpression, userAttributes, referenceValue, "==");
        }
        
        if (trimmedExpression.contains(" != ")) {
            return evaluateComparisonExpression(trimmedExpression, userAttributes, referenceValue, "!=");
        }
        
        // Handle boolean expressions
        if (userAttributes.containsKey(trimmedExpression)) {
            Object value = userAttributes.get(trimmedExpression);
            return Boolean.parseBoolean(value.toString());
        }
        
        throw new RuntimeException("Unsupported expression: " + expression);
    }

    private boolean evaluateInExpression(String expression, Map<String, Object> userAttributes, Object referenceValue) {
        String[] parts = expression.split(" IN ");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid IN expression: " + expression);
        }
        
        String attributeName = parts[0].trim();
        Object attributeValue = userAttributes.get(attributeName);
        
        if (attributeValue == null) {
            return false;
        }
        
        // Use reference value if available, otherwise parse the expression
        if (referenceValue instanceof List) {
            List<?> allowedValues = (List<?>) referenceValue;
            return allowedValues.contains(attributeValue.toString());
        }
        
        // Parse inline list (fallback)
        String listPart = parts[1].trim();
        if (listPart.startsWith("[") && listPart.endsWith("]")) {
            String listContent = listPart.substring(1, listPart.length() - 1);
            String[] values = listContent.split(",");
            for (String value : values) {
                String cleanValue = value.trim().replace("'", "").replace("\"", "");
                if (cleanValue.equals(attributeValue.toString())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean evaluateComparisonExpression(String expression, Map<String, Object> userAttributes, 
                                               Object referenceValue, String operator) {
        String[] parts = expression.split(" " + Pattern.quote(operator) + " ");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid comparison expression: " + expression);
        }
        
        String attributeName = parts[0].trim();
        Object attributeValue = userAttributes.get(attributeName);
        
        if (attributeValue == null) {
            return false;
        }
        
        Object compareValue = referenceValue;
        if (compareValue == null) {
            // Try to parse the right side of the expression
            String rightSide = parts[1].trim();
            try {
                compareValue = Double.parseDouble(rightSide);
            } catch (NumberFormatException e) {
                compareValue = rightSide;
            }
        }
        
        return compareValues(attributeValue, compareValue, operator);
    }

    private boolean compareValues(Object left, Object right, String operator) {
        try {
            // Handle numeric comparisons
            if (left instanceof Number && right instanceof Number) {
                double leftVal = ((Number) left).doubleValue();
                double rightVal = ((Number) right).doubleValue();
                
                return switch (operator) {
                    case ">" -> leftVal > rightVal;
                    case ">=" -> leftVal >= rightVal;
                    case "<" -> leftVal < rightVal;
                    case "<=" -> leftVal <= rightVal;
                    case "==" -> Double.compare(leftVal, rightVal) == 0;
                    case "!=" -> Double.compare(leftVal, rightVal) != 0;
                    default -> throw new RuntimeException("Unsupported operator: " + operator);
                };
            }
            
            // Handle string comparisons
            String leftStr = left.toString();
            String rightStr = right.toString();
            
            return switch (operator) {
                case "==" -> leftStr.equals(rightStr);
                case "!=" -> !leftStr.equals(rightStr);
                case ">" -> leftStr.compareTo(rightStr) > 0;
                case ">=" -> leftStr.compareTo(rightStr) >= 0;
                case "<" -> leftStr.compareTo(rightStr) < 0;
                case "<=" -> leftStr.compareTo(rightStr) <= 0;
                default -> throw new RuntimeException("Unsupported operator for strings: " + operator);
            };
            
        } catch (Exception e) {
            throw new RuntimeException("Error comparing values: " + e.getMessage(), e);
        }
    }
}
