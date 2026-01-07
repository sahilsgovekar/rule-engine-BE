# Rule-Based Risk Evaluation Engine - Sample Run Guide

This document provides comprehensive examples of how to use the Rule-Based Risk Evaluation Engine API. The application demonstrates a dynamic rule evaluation system for loan provider services.

## Prerequisites

- Java 17+
- Maven 3.6+
- Application running on http://localhost:8080

## Starting the Application

```bash
cd rule-engine
mvn spring-boot:run
```

The application will start on port 8080 and automatically populate sample data.

## API Documentation

Once the application is running, you can access:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Database Console**: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb, User: sa, Password: password)

## Sample Data Overview

The application automatically creates:
- **7 Documents**: Reference values for rules (min age, cities, income thresholds, etc.)
- **6 Rules**: Binary decision tree rules for loan evaluation
- **2 Policies**: Collections of rules for different loan types

## Sample API Calls

### 1. Document Management

#### Create a Document
```bash
curl -X POST "http://localhost:8080/api/documents" \
-H "Content-Type: application/json" \
-d '{
  "documentId": "doc_new_city",
  "documentValue": "Pune",
  "valueType": "STRING"
}'
```

#### Get All Documents
```bash
curl -X GET "http://localhost:8080/api/documents"
```

#### Get Document by ID
```bash
curl -X GET "http://localhost:8080/api/documents/doc_min_age"
```

#### Update Document
```bash
curl -X PUT "http://localhost:8080/api/documents/doc_min_age" \
-H "Content-Type: application/json" \
-d '{
  "documentId": "doc_min_age",
  "documentValue": "21",
  "valueType": "INTEGER"
}'
```

### 2. Rule Management

#### Create a New Rule
```bash
curl -X POST "http://localhost:8080/api/rules" \
-H "Content-Type: application/json" \
-d '{
  "ruleId": "rule_credit_score",
  "expression": "creditScore >= 700",
  "referenceId": "doc_min_credit_score",
  "onTrueType": "VALUE",
  "onTrueValue": "true",
  "onFalseType": "VALUE", 
  "onFalseValue": "false",
  "description": "Check minimum credit score requirement"
}'
```

#### Get All Rules
```bash
curl -X GET "http://localhost:8080/api/rules"
```

#### Get Active Rules Only
```bash
curl -X GET "http://localhost:8080/api/rules/active"
```

#### Get Rule by ID
```bash
curl -X GET "http://localhost:8080/api/rules/rule_age_check"
```

#### Update a Rule
```bash
curl -X PUT "http://localhost:8080/api/rules/rule_age_check" \
-H "Content-Type: application/json" \
-d '{
  "ruleId": "rule_age_check",
  "expression": "age >= 21",
  "referenceId": "doc_min_age",
  "onTrueType": "RULE",
  "onTrueValue": "rule_city_check",
  "onFalseType": "VALUE",
  "onFalseValue": "false",
  "description": "Updated: Check if user is at least 21 years old"
}'
```

#### Activate/Deactivate Rule
```bash
# Deactivate
curl -X PATCH "http://localhost:8080/api/rules/rule_age_check/deactivate"

# Activate
curl -X PATCH "http://localhost:8080/api/rules/rule_age_check/activate"
```

#### Search Rules by Expression
```bash
curl -X GET "http://localhost:8080/api/rules/search?keyword=age"
```

### 3. Policy Management

#### Create a New Policy
```bash
curl -X POST "http://localhost:8080/api/policies" \
-H "Content-Type: application/json" \
-d '{
  "policyId": "policy_premium_loan",
  "policyName": "Premium Loan Policy",
  "description": "Policy for premium customers with higher loan amounts",
  "rootRuleId": "rule_age_check",
  "ruleIds": ["rule_age_check", "rule_income_check", "rule_amount_check"],
  "priority": 3
}'
```

#### Get All Policies
```bash
curl -X GET "http://localhost:8080/api/policies"
```

#### Get Active Policies
```bash
curl -X GET "http://localhost:8080/api/policies/active"
```

#### Get Policy by ID
```bash
curl -X GET "http://localhost:8080/api/policies/policy_standard_loan"
```

### 4. Rule Evaluation Examples

#### Evaluate Single Rule - Successful Case
```bash
curl -X POST "http://localhost:8080/api/rules/rule_age_check/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user123",
  "userAttributes": {
    "age": 25,
    "city": "Bangalore",
    "income": 50000,
    "loanAmount": 200000
  }
}'
```

**Expected Response:**
```json
{
  "result": true,
  "userId": "user123",
  "evaluatedId": "rule_age_check",
  "evaluationType": "RULE",
  "executionTrace": [
    {
      "ruleId": "rule_age_check",
      "expression": "age >= 18",
      "evaluationResult": true,
      "nextAction": "onTrue: rule_city_check"
    },
    {
      "ruleId": "rule_city_check", 
      "expression": "city IN allowedCities",
      "evaluationResult": true,
      "nextAction": "onTrue: rule_income_check"
    }
  ],
  "evaluatedAt": "2024-01-08T02:23:14.123",
  "errorMessage": null
}
```

#### Evaluate Single Rule - Failed Case (Underage)
```bash
curl -X POST "http://localhost:8080/api/rules/rule_age_check/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user456",
  "userAttributes": {
    "age": 17,
    "city": "Bangalore", 
    "income": 50000,
    "loanAmount": 200000
  }
}'
```

#### Evaluate Single Rule - Failed Case (Wrong City)
```bash
curl -X POST "http://localhost:8080/api/rules/rule_city_check/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user789",
  "userAttributes": {
    "age": 25,
    "city": "Kolkata",
    "income": 50000,
    "loanAmount": 200000
  }
}'
```

### 5. Policy Evaluation Examples

#### Evaluate Standard Loan Policy - Approved Case
```bash
curl -X POST "http://localhost:8080/api/policies/policy_standard_loan/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user_approved",
  "userAttributes": {
    "age": 30,
    "city": "Mumbai",
    "income": 75000,
    "loanAmount": 300000
  }
}'
```

#### Evaluate Standard Loan Policy - Rejected Case (Low Income)
```bash
curl -X POST "http://localhost:8080/api/policies/policy_standard_loan/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user_rejected",
  "userAttributes": {
    "age": 28,
    "city": "Delhi",
    "income": 20000,
    "loanAmount": 100000
  }
}'
```

#### Evaluate Standard Loan Policy - High Amount Case
```bash
curl -X POST "http://localhost:8080/api/policies/policy_standard_loan/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user_high_amount",
  "userAttributes": {
    "age": 35,
    "city": "Chennai",
    "income": 100000,
    "loanAmount": 600000
  }
}'
```

#### Evaluate Simple Loan Policy
```bash
curl -X POST "http://localhost:8080/api/policies/policy_simple_loan/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user_simple",
  "userAttributes": {
    "age": 25
  }
}'
```

### 6. Advanced Use Cases

#### Complex Rule Creation with Multiple Conditions
```bash
# First create the reference document
curl -X POST "http://localhost:8080/api/documents" \
-H "Content-Type: application/json" \
-d '{
  "documentId": "doc_max_loan_amount",
  "documentValue": "1000000",
  "valueType": "INTEGER"
}'

# Then create the rule
curl -X POST "http://localhost:8080/api/rules" \
-H "Content-Type: application/json" \
-d '{
  "ruleId": "rule_max_loan_check",
  "expression": "loanAmount <= 1000000",
  "referenceId": "doc_max_loan_amount",
  "onTrueType": "VALUE",
  "onTrueValue": "true",
  "onFalseType": "VALUE",
  "onFalseValue": "false",
  "description": "Check maximum loan amount limit"
}'
```

#### Create Dynamic Policy Based on Business Rules
```bash
curl -X POST "http://localhost:8080/api/policies" \
-H "Content-Type: application/json" \
-d '{
  "policyId": "policy_startup_loan",
  "policyName": "Startup Business Loan Policy",
  "description": "Specialized policy for startup business loans",
  "rootRuleId": "rule_age_check",
  "ruleIds": ["rule_age_check", "rule_city_check", "rule_max_loan_check"],
  "priority": 5
}'
```

### 7. Testing Edge Cases

#### Test with Missing User Attributes
```bash
curl -X POST "http://localhost:8080/api/rules/rule_age_check/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user_incomplete",
  "userAttributes": {
    "city": "Bangalore"
  }
}'
```

#### Test with Invalid Rule Reference
```bash
curl -X POST "http://localhost:8080/api/rules/rule_nonexistent/evaluate" \
-H "Content-Type: application/json" \
-d '{
  "userId": "user_test",
  "userAttributes": {
    "age": 25
  }
}'
```

### 8. Management Operations

#### Get Rule Statistics
```bash
curl -X GET "http://localhost:8080/api/rules/count/active"
```

#### List Recent Documents
```bash
curl -X GET "http://localhost:8080/api/documents/recent"
```

#### Delete Rule (be careful with dependencies)
```bash
curl -X DELETE "http://localhost:8080/api/rules/rule_credit_score"
```

#### Delete Policy
```bash
curl -X DELETE "http://localhost:8080/api/policies/policy_premium_loan"
```

## Business Logic Flow

### Standard Loan Policy Flow
1. **Age Check**: User must be >= 18 years
2. **City Check**: User must be from allowed cities (Bangalore, Mumbai, Delhi, Chennai)
3. **Income Check**: User must have income >= 25,000
4. **Amount Check**: If loan amount < 500,000 → Approve, else continue to high amount check
5. **High Amount Check**: For loans >= 500,000, user must be <= 65 years

### Rule Evaluation Process
- Rules are evaluated in a binary tree structure
- Each rule has onTrue and onFalse outcomes
- Outcomes can either be:
  - **VALUE**: Terminal decision (true/false)
  - **RULE**: Reference to another rule to evaluate next
- Execution trace shows the path taken through the rules

## Error Handling

The API handles various error scenarios:
- Invalid rule/policy IDs
- Missing user attributes
- Circular rule dependencies
- Invalid expressions
- Database constraint violations

## Performance Considerations

- Rule evaluation typically completes in < 50ms
- The system supports complex nested rule structures
- Binary tree approach ensures deterministic outcomes
- Audit trail is maintained for all evaluations

## Customization Examples

### Adding New Attribute Types
You can extend the system to handle new user attributes by:
1. Creating appropriate documents with reference values
2. Writing rules with new expression patterns
3. Updating policies to include the new rules

### Creating Complex Business Logic
Example: Multi-factor authentication requirement
```bash
curl -X POST "http://localhost:8080/api/rules" \
-H "Content-Type: application/json" \
-d '{
  "ruleId": "rule_mfa_required",
  "expression": "loanAmount >= 100000",
  "referenceId": "doc_mfa_threshold",
  "onTrueType": "RULE",
  "onTrueValue": "rule_verify_mfa",
  "onFalseType": "VALUE",
  "onFalseValue": "true",
  "description": "Check if MFA is required based on loan amount"
}'
```

This comprehensive guide covers all major functionality of the Rule-Based Risk Evaluation Engine. Use these examples to understand the system capabilities and build your own custom rules and policies.
