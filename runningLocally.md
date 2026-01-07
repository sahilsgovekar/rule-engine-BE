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

## Troubleshooting

### Common Issues and Solutions

#### Issue 1: Port Already in Use
**Error**: `Port 8080 was already in use`  
**Solution**:
```bash
# Find process using port 8080
lsof -i :8080  # On macOS/Linux
netstat -ano | findstr :8080  # On Windows

# Kill the process or change port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

#### Issue 2: Java Version Issues
**Error**: `UnsupportedClassVersionError`  
**Solution**: 
- Ensure Java 17+ is installed
- Set JAVA_HOME environment variable
```bash
export JAVA_HOME=/path/to/java17  # On macOS/Linux
set JAVA_HOME=C:\Path\To\Java17   # On Windows
```

#### Issue 3: Maven Dependencies Not Downloading
**Error**: `Could not resolve dependencies`  
**Solution**:
```bash
mvn clean install -U  # Force update dependencies
mvn dependency:purge-local-repository  # Clear local cache
```

#### Issue 4: Database Connection Issues
**Error**: `Failed to configure a DataSource`  
**Solution**: The application uses H2 in-memory database by default. If issues persist:
```bash
mvn clean compile  # Rebuild project
rm -rf ~/.m2/repository/com/h2database  # Clear H2 cache
```

#### Issue 5: Lombok Issues
**Error**: `cannot find symbol` for getters/setters  
**Solution**:
- Enable annotation processing in your IDE
- Or compile with: `mvn clean compile -Dmaven.compiler.annotationProcessorPaths=org.projectlombok:lombok`

### Performance Tuning

#### Increase Memory (if needed)
```bash
export MAVEN_OPTS="-Xmx2g -Xms1g"  # On macOS/Linux
set MAVEN_OPTS=-Xmx2g -Xms1g       # On Windows
mvn spring-boot:run
```

#### Enable Debug Logging
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.lps.ruleengine=DEBUG"
```

## Development Mode

### Hot Reload with Spring Boot DevTools
For development, you can enable automatic restart on code changes:

1. Add DevTools dependency (already included in project)
2. Run: `mvn spring-boot:run`
3. Make code changes - application will automatically restart

### Running in IDE
1. **IntelliJ IDEA**: Right-click on `RuleEngineApplication.java` → Run
2. **Eclipse**: Right-click on project → Run As → Spring Boot App
3. **VS Code**: Use Spring Boot extension and press F5

## Production Considerations

### Building JAR for Production
```bash
mvn clean package -DskipTests
cd ruleengine-controller
java -jar target/ruleengine-controller-1.0.0.jar
```

### Environment-Specific Configuration
Create `application-prod.yml` for production settings:
```yaml
server:
  port: 9090

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ruleengine
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Run with profile:
```bash
java -jar target/rule-engine-1.0.0.jar --spring.profiles.active=prod
```

## Next Steps

After successful startup:

1. **Explore Swagger UI**: http://localhost:8080/swagger-ui.html
2. **Review Sample Data**: Check H2 console to see created entities
3. **Test API Calls**: Use the examples in `sampleRun.md`
4. **Create Custom Rules**: Use the REST API to add your business logic

## Support

If you encounter issues:
1. Check the console logs for specific error messages
2. Verify all prerequisites are met
3. Ensure no firewall/antivirus is blocking the application
4. Try running with debug enabled: `--debug`

---

**🎉 Congratulations!** Your Rule-Based Risk Evaluation Engine is now running locally and ready for testing and development.
