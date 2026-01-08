# Rule Engine Service API Documentation

## Base URL
```
http://localhost:8080
```

## Overview
The Rule Engine Service provides APIs for managing rules, policies, documents, and evaluating business rules for loan processing. This service supports creating complex rule chains, policy management, and real-time evaluation capabilities.

---

## Table of Contents
1. [Rule Management APIs](#rule-management-apis)
2. [Policy Management APIs](#policy-management-apis)
3. [Document Management APIs](#document-management-apis)
4. [Policy & Rule Evaluation APIs](#policy--rule-evaluation-apis)
5. [Data Models](#data-models)

---

## Rule Management APIs

### 1. Create a New Rule
**Endpoint:** `POST /api/rules`  
**Description:** Creates a new business rule with specified conditions and outcomes.

**Request Body:**
```json
{
  "ruleId": "rule_age_check",
  "expression": "age > 18",
  "referenceId": "doc_min_age",
  "onTrueType": "VALUE",
  "onTrueValue": "true",
  "onFalseType": "VALUE", 
  "onFalseValue": "false",
  "description": "Check if user is above minimum age"
}
```

**Sample cURL:**
```bash
curl -X POST http://localhost:8080/api/rules \
  -H "Content-Type: application/json" \
  -d '{
    "ruleId": "rule_age_check",
    "expression": "age > 18",
    "referenceId": "doc_min_age",
    "onTrueType": "VALUE",
    "onTrueValue": "true",
    "onFalseType": "VALUE",
    "onFalseValue": "false",
    "description": "Check if user is above minimum age"
  }'
```

### 2. Get All Rules
**Endpoint:** `GET /api/rules`  
**Description:** Retrieves all rules in the system.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/rules
```

### 3. Get Active Rules Only
**Endpoint:** `GET /api/rules/active`  
**Description:** Retrieves only active rules.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/rules/active
```

### 4. Get Rule by ID
**Endpoint:** `GET /api/rules/{ruleId}`  
**Description:** Retrieves a specific rule by its ID.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/rules/rule_age_check
```

### 5. Update an Existing Rule
**Endpoint:** `PUT /api/rules/{ruleId}`  
**Description:** Updates an existing rule with new parameters.

**Sample cURL:**
```bash
curl -X PUT http://localhost:8080/api/rules/rule_age_check \
  -H "Content-Type: application/json" \
  -d '{
    "ruleId": "rule_age_check",
    "expression": "age >= 21",
    "referenceId": "doc_min_age",
    "onTrueType": "VALUE",
    "onTrueValue": "true",
    "onFalseType": "VALUE",
    "onFalseValue": "false",
    "description": "Updated age check rule"
  }'
```

### 6. Delete a Rule
**Endpoint:** `DELETE /api/rules/{ruleId}`  
**Description:** Deletes a rule from the system.

**Sample cURL:**
```bash
curl -X DELETE http://localhost:8080/api/rules/rule_age_check
```

### 7. Activate a Rule
**Endpoint:** `PATCH /api/rules/{ruleId}/activate`  
**Description:** Sets a rule as active.

**Sample cURL:**
```bash
curl -X PATCH http://localhost:8080/api/rules/rule_age_check/activate
```

### 8. Deactivate a Rule
**Endpoint:** `PATCH /api/rules/{ruleId}/deactivate`  
**Description:** Sets a rule as inactive.

**Sample cURL:**
```bash
curl -X PATCH http://localhost:8080/api/rules/rule_age_check/deactivate
```

### 9. Evaluate a Single Rule
**Endpoint:** `POST /api/rules/{ruleId}/evaluate`  
**Description:** Evaluates a rule against user attributes.

**Request Body:**
```json
{
  "userId": "user123",
  "userAttributes": {
    "age": 25,
    "income": 50000,
    "creditScore": 750
  }
}
```

**Sample cURL:**
```bash
curl -X POST http://localhost:8080/api/rules/rule_age_check/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "userAttributes": {
      "age": 25,
      "income": 50000,
      "creditScore": 750
    }
  }'
```

### 10. Search Rules by Expression
**Endpoint:** `GET /api/rules/search?keyword={keyword}`  
**Description:** Searches for rules containing a keyword in their expression.

**Sample cURL:**
```bash
curl -X GET "http://localhost:8080/api/rules/search?keyword=age"
```

### 11. Get Active Rule Count
**Endpoint:** `GET /api/rules/count/active`  
**Description:** Returns the count of active rules.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/rules/count/active
```

---

## Policy Management APIs

### 1. Create a New Policy
**Endpoint:** `POST /api/policies`  
**Description:** Creates a new policy with specified rules.

**Request Body:**
```json
{
  "policyId": "policy_loan_approval",
  "policyName": "Loan Approval Policy",
  "description": "Main policy for loan approval decisions",
  "rootRuleId": "rule_age_check",
  "ruleIds": ["rule_age_check", "rule_income_check", "rule_credit_check"],
  "priority": 1
}
```

**Sample cURL:**
```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "policyId": "policy_loan_approval",
    "policyName": "Loan Approval Policy", 
    "description": "Main policy for loan approval decisions",
    "rootRuleId": "rule_age_check",
    "ruleIds": ["rule_age_check", "rule_income_check", "rule_credit_check"],
    "priority": 1
  }'
```

### 2. Get All Policies
**Endpoint:** `GET /api/policies`  
**Description:** Retrieves all policies in the system.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/policies
```

### 3. Get Active Policies Only
**Endpoint:** `GET /api/policies/active`  
**Description:** Retrieves only active policies.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/policies/active
```

### 4. Get Policy by ID
**Endpoint:** `GET /api/policies/{policyId}`  
**Description:** Retrieves a specific policy by its ID.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/policies/policy_loan_approval
```

### 5. Update an Existing Policy
**Endpoint:** `PUT /api/policies/{policyId}`  
**Description:** Updates an existing policy with new parameters.

**Sample cURL:**
```bash
curl -X PUT http://localhost:8080/api/policies/policy_loan_approval \
  -H "Content-Type: application/json" \
  -d '{
    "policyId": "policy_loan_approval",
    "policyName": "Updated Loan Approval Policy",
    "description": "Updated policy for loan approval decisions",
    "rootRuleId": "rule_age_check",
    "ruleIds": ["rule_age_check", "rule_income_check"],
    "priority": 2
  }'
```

### 6. Delete a Policy
**Endpoint:** `DELETE /api/policies/{policyId}`  
**Description:** Deletes a policy from the system.

**Sample cURL:**
```bash
curl -X DELETE http://localhost:8080/api/policies/policy_loan_approval
```

### 7. Activate a Policy
**Endpoint:** `PATCH /api/policies/{policyId}/activate`  
**Description:** Sets a policy as active.

**Sample cURL:**
```bash
curl -X PATCH http://localhost:8080/api/policies/policy_loan_approval/activate
```

### 8. Deactivate a Policy
**Endpoint:** `PATCH /api/policies/{policyId}/deactivate`  
**Description:** Sets a policy as inactive.

**Sample cURL:**
```bash
curl -X PATCH http://localhost:8080/api/policies/policy_loan_approval/deactivate
```

---

## Document Management APIs

### 1. Create a New Document
**Endpoint:** `POST /api/documents`  
**Description:** Creates a new document with specified value (reference values for rules).

**Request Body:**
```json
{
  "documentId": "doc_min_age",
  "documentValue": "18",
  "valueType": "INTEGER"
}
```

**Sample cURL:**
```bash
curl -X POST http://localhost:8080/api/documents \
  -H "Content-Type: application/json" \
  -d '{
    "documentId": "doc_min_age",
    "documentValue": "18",
    "valueType": "INTEGER"
  }'
```

### 2. Get All Documents
**Endpoint:** `GET /api/documents`  
**Description:** Retrieves all documents in the system.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/documents
```

### 3. Get Document by ID
**Endpoint:** `GET /api/documents/{documentId}`  
**Description:** Retrieves a specific document by its ID.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/documents/doc_min_age
```

### 4. Get Documents by Type
**Endpoint:** `GET /api/documents/type/{valueType}`  
**Description:** Retrieves documents filtered by value type.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/documents/type/INTEGER
```

### 5. Update an Existing Document
**Endpoint:** `PUT /api/documents/{documentId}`  
**Description:** Updates an existing document with new value.

**Sample cURL:**
```bash
curl -X PUT http://localhost:8080/api/documents/doc_min_age \
  -H "Content-Type: application/json" \
  -d '{
    "documentId": "doc_min_age",
    "documentValue": "21",
    "valueType": "INTEGER"
  }'
```

### 6. Delete a Document
**Endpoint:** `DELETE /api/documents/{documentId}`  
**Description:** Deletes a document from the system.

**Sample cURL:**
```bash
curl -X DELETE http://localhost:8080/api/documents/doc_min_age
```

### 7. Get Recent Documents
**Endpoint:** `GET /api/documents/recent`  
**Description:** Retrieves recently created documents.

**Sample cURL:**
```bash
curl -X GET http://localhost:8080/api/documents/recent
```

---

## Policy & Rule Evaluation APIs

### 1. Evaluate a Policy (Primary Integration Endpoint)
**Endpoint:** `POST /api/evaluation/policies/{policyId}`  
**Description:** Evaluates a policy against user attributes to determine loan eligibility. This is the primary endpoint used by client applications for loan decisions.

**Request Body:**
```json
{
  "userId": "user123",
  "userAttributes": {
    "age": 25,
    "income": 50000,
    "creditScore": 750,
    "employmentStatus": "employed",
    "loanAmount": 25000
  }
}
```

**Sample cURL:**
```bash
curl -X POST http://localhost:8080/api/evaluation/policies/policy_loan_approval \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "userAttributes": {
      "age": 25,
      "income": 50000,
      "creditScore": 750,
      "employmentStatus": "employed",
      "loanAmount": 25000
    }
  }'
```

**Response Example:**
```json
{
  "userId": "user123",
  "policyId": "policy_loan_approval",
  "approved": true,
  "finalDecision": true,
  "confidence": 0.95,
  "reason": "All conditions met for loan approval",
  "executionTrace": [
    {
      "ruleId": "rule_age_check",
      "expression": "age > 18",
      "result": true,
      "executionTime": 10
    }
  ],
  "evaluatedAt": "2026-01-08T08:24:00Z",
  "executionTimeMs": 50
}
```

### 2. Evaluate a Single Rule
**Endpoint:** `POST /api/evaluation/rules/{ruleId}`  
**Description:** Directly evaluates a single rule against user attributes. Useful for testing individual rules or lightweight evaluations.

**Sample cURL:**
```bash
curl -X POST http://localhost:8080/api/evaluation/rules/rule_age_check \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "userAttributes": {
      "age": 25,
      "income": 50000,
      "creditScore": 750
    }
  }'
```

### 3. Bulk Policy Evaluation
**Endpoint:** `POST /api/evaluation/policies/bulk?policyIds={policyId1}&policyIds={policyId2}`  
**Description:** Evaluates multiple policies for the same user to compare outcomes. Useful for A/B testing or policy comparison scenarios.

**Sample cURL:**
```bash
curl -X POST "http://localhost:8080/api/evaluation/policies/bulk?policyIds=policy_loan_approval&policyIds=policy_premium_loan" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "userAttributes": {
      "age": 25,
      "income": 50000,
      "creditScore": 750,
      "employmentStatus": "employed",
      "loanAmount": 25000
    }
  }'
```

---

## Data Models

### Rule Model
```json
{
  "ruleId": "string",
  "expression": "string",
  "referenceId": "string",
  "onTrueType": "VALUE|RULE",
  "onTrueValue": "string",
  "onFalseType": "VALUE|RULE", 
  "onFalseValue": "string",
  "description": "string",
  "isActive": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "version": "integer"
}
```

### Policy Model
```json
{
  "policyId": "string",
  "policyName": "string",
  "description": "string",
  "rootRuleId": "string",
  "ruleIds": ["string"],
  "priority": "integer",
  "isActive": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Document Model
```json
{
  "documentId": "string",
  "documentValue": "string",
  "valueType": "STRING|INTEGER|DOUBLE|BOOLEAN",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Evaluation Request Model
```json
{
  "userId": "string",
  "userAttributes": {
    "key": "value"
  }
}
```

### Evaluation Response Model
```json
{
  "userId": "string",
  "policyId": "string",
  "ruleId": "string",
  "approved": "boolean",
  "finalDecision": "boolean",
  "confidence": "double",
  "reason": "string",
  "executionTrace": [
    {
      "ruleId": "string",
      "expression": "string",
      "result": "boolean",
      "executionTime": "integer"
    }
  ],
  "evaluatedAt": "datetime",
  "executionTimeMs": "long"
}
```

---

## Integration Guidelines

### Authentication
Currently, the service does not require authentication. For production use, implement appropriate security measures.

### Error Handling
- **400 Bad Request:** Invalid request data or validation errors
- **404 Not Found:** Resource not found
- **500 Internal Server Error:** Server-side errors

### Rate Limiting
No rate limiting is currently implemented. Consider implementing rate limiting for production use.

### Content Type
All requests should use `Content-Type: application/json` header.

### Response Format
All responses are in JSON format with appropriate HTTP status codes.

---

## Development URLs

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs
- **H2 Console:** http://localhost:8080/h2-console

---

## Support

For technical support or questions regarding API integration, please contact the development team.
