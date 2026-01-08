# Running the Rule-Based Risk Evaluation Engine Locally

This guide provides step-by-step instructions to set up and run the Rule-Based Risk Evaluation Engine on your local machine.

## Prerequisites

### Required Software
1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version`

2. **Apache Maven 3.6 or higher**
   - Download from [Apache Maven](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **Git (optional, for cloning)**
   - Download from [Git](https://git-scm.com/downloads)

### System Requirements
- **RAM**: Minimum 4GB, Recommended 8GB+
- **Disk Space**: At least 500MB free space
- **Operating System**: Windows 10+, macOS 10.14+, or Linux

## Installation Steps

### Step 1: Verify Java Installation
```bash
java -version
```
Expected output should show Java 17 or higher:
```
java version "17.0.x" 2023-xx-xx
Java(TM) SE Runtime Environment (build 17.0.x)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x)
```

### Step 2: Verify Maven Installation
```bash
mvn -version
```
Expected output:
```
Apache Maven 3.x.x
Maven home: /path/to/maven
Java version: 17.0.x
```

### Step 3: Navigate to Project Directory
```bash
cd rule-engine
```

### Step 4: Build the Project
```bash
mvn clean install
```
This will:
- Download all required dependencies (~150MB)
- Compile all modules in the correct order
- Install modules to local Maven repository
- Validate the project structure

### Step 5: Run Tests (Optional but Recommended)
```bash
mvn test
```
This ensures everything is working correctly before starting the application.

### Step 6: Start the Application

**Option 1 (Recommended): Run from controller module**
```bash
cd ruleengine-controller
mvn spring-boot:run
```

**Option 2: Run from root directory**
```bash
mvn spring-boot:run -pl ruleengine-controller
```

**Option 3: Build and run JAR**
```bash
mvn clean package -DskipTests
cd ruleengine-controller
java -jar target/ruleengine-controller-1.0.0.jar
```

**Important Note:** Always build from the root directory first using `mvn clean install` before running the application. This ensures all internal module dependencies are available in your local Maven repository.

## Application Startup

### Expected Startup Logs
You should see logs similar to:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.2.1)

2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] RuleEngineApplication : Starting RuleEngineApplication
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] DataInitializer : Initializing sample data...
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] DataInitializer : Created 7 sample documents
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] DataInitializer : Created 6 sample rules
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] DataInitializer : Created 2 sample policies
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] DataInitializer : Sample data initialization completed
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] TomcatWebServer : Tomcat started on port(s): 8080 (http)
2024-xx-xxTxx:xx:xx.xxx INFO [rule-engine] [main] RuleEngineApplication : Started RuleEngineApplication in x.xxx seconds
```

### Success Indicators
✅ No error messages in console  
✅ "Tomcat started on port(s): 8080" appears  
✅ "Started RuleEngineApplication" appears  
✅ Sample data initialization completes  

## Accessing the Application

Once the application starts successfully, you can access:

### 🌐 Web Interfaces
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/v3/api-docs
- **H2 Database Console**: http://localhost:8080/h2-console

### 🔧 H2 Database Console Access
1. Open: http://localhost:8080/h2-console
2. Use these settings:
   - **JDBC URL**: `jdbc:h2:mem:ruleenginedb`
   - **User Name**: `sa`
   - **Password**: `password`
3. Click "Connect"

### 📊 Sample Data Verification
After startup, the application automatically creates:
- **7 Documents**: Reference values for rule evaluation
- **6 Rules**: Binary decision tree rules for loan approval
- **2 Policies**: Standard and Simple loan approval policies

## Quick API Test

### Test 1: Get All Rules
```bash
curl -X GET "http://localhost:8080/api/rules"
```

### Test 2: Evaluate a Policy
```bash
curl -X POST "http://localhost:8080/api/evaluation/policies/policy_standard_loan" \
-H "Content-Type: application/json" \
-d '{
  "userId": "test_user",
  "userAttributes": {
    "age": 25,
    "city": "Bangalore",
    "income": 50000,
    "loanAmount": 200000
  }
}'
```

Expected response should show `"result": true` for approval.