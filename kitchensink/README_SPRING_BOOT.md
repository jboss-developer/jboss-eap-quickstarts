# Kitchensink Spring Boot Application with MongoDB

A modern member registration application migrated from Jakarta EE/JBoss EAP to Spring Boot 3.4.0 with MongoDB. This application demonstrates best practices for building web applications with Spring Boot, including MVC controllers, REST APIs, MongoDB data access, and validation.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Building the Application](#building-the-application)
- [Running the Application](#running-the-application)
- [Accessing the Application](#accessing-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Database](#database)
- [Project Structure](#project-structure)
- [Migration Notes](#migration-notes)

## Overview

The Kitchensink application is a simple member registration system that allows users to:
- View a list of registered members
- Register new members with validation
- Access member data via RESTful APIs

This application was originally built for JBoss EAP using Jakarta EE technologies and has been successfully migrated to Spring Boot, maintaining all original functionality while modernizing the technology stack.

## Features

### Web UI
- **Member Registration Form**: Thymeleaf-based web interface for registering new members
- **Member List**: Display all registered members ordered alphabetically by name
- **Validation Feedback**: Real-time form validation with user-friendly error messages
- **Responsive Design**: Clean, modern UI that works on all devices

### REST API
- **List Members**: Retrieve all members via REST API
- **Get Member by ID**: Lookup individual members
- **Register Member**: POST endpoint for member registration
- **Duplicate Detection**: Automatic duplicate email validation
- **JSON Support**: Full JSON request/response support

### Data Validation
- **Name**: 1-25 characters, no numbers allowed
- **Email**: Valid email format, must be unique
- **Phone**: 10-12 digits, numbers only

## Technology Stack

- **Spring Boot**: 3.4.0
- **Java**: 21
- **Spring MVC**: Web layer and REST controllers
- **Spring Data MongoDB**: Data access layer for MongoDB
- **MongoDB**: NoSQL document database
- **Thymeleaf**: Template engine for server-side rendering
- **Bean Validation**: Jakarta Bean Validation 3.0
- **JUnit 5**: Testing framework
- **Embedded MongoDB**: For testing (Flapdoodle)
- **Maven**: Build and dependency management

## Prerequisites

Before building and running the application, ensure you have:

- **Java 21** or higher installed
  ```bash
  java -version
  ```
- **Maven 3.6+** installed
  ```bash
  mvn -version
  ```
- **MongoDB 5.0+** installed and running
  ```bash
  # On macOS with Homebrew
  brew install mongodb-community
  brew services start mongodb-community

  # On Linux (Ubuntu/Debian)
  sudo systemctl start mongod

  # Verify MongoDB is running
  mongosh --eval "db.version()"
  ```
- **Git** (to clone the repository)

## Building the Application

### 1. Clone the Repository

```bash
git clone <repository-url>
cd jboss-eap-quickstarts/kitchensink
```

### 2. Build with Maven

Build the application and run all tests:

```bash
mvn clean package
```

Build without running tests (faster):

```bash
mvn clean package -DskipTests
```

The build will create an executable JAR file in the `target` directory:
```
target/kitchensink-8.0.0.GA.jar
```

## Running the Application

### Option 1: Using Maven

Run directly with Maven (useful for development):

```bash
mvn spring-boot:run
```

### Option 2: Using the JAR file

Run the packaged JAR:

```bash
java -jar target/kitchensink-8.0.0.GA.jar
```

### Application Startup

When the application starts, you'll see output like:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.0)

...
Tomcat started on port 8080
Started KitchensinkApplication in X.XXX seconds
```

## Accessing the Application

### Web Interface

Open your browser and navigate to:

```
http://localhost:8080
```

You'll see the member registration form and a table of all registered members.

### H2 Database Console (Development)

Access the H2 database console at:

```
http://localhost:8080/h2-console
```

**Connection Details:**
- **JDBC URL**: `jdbc:h2:mem:kitchensink`
- **Username**: `sa`
- **Password**: (leave empty)

## API Endpoints

### REST API Base URL

```
http://localhost:8080/rest/members
```

### Available Endpoints

#### 1. List All Members

```http
GET /rest/members
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phoneNumber": "2125551234"
  },
  {
    "id": 2,
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "phoneNumber": "5551234567"
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/rest/members
```

#### 2. Get Member by ID

```http
GET /rest/members/{id}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "2125551234"
}
```

**Response (404 Not Found):**
```
(empty response with 404 status)
```

**cURL Example:**
```bash
curl http://localhost:8080/rest/members/1
```

#### 3. Register New Member

```http
POST /rest/members
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "2125551234"
}
```

**Response (200 OK):**
```
(empty response body)
```

**Response (400 Bad Request) - Validation Error:**
```json
{
  "name": "Must not contain numbers",
  "email": "must be a well-formed email address",
  "phoneNumber": "size must be between 10 and 12"
}
```

**Response (409 Conflict) - Duplicate Email:**
```json
{
  "email": "Email taken"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/rest/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phoneNumber": "2125551234"
  }'
```

## Testing

The application includes comprehensive test coverage:

### Run All Tests

```bash
mvn test
```

### Test Categories

1. **Unit Tests** (21 tests)
   - `MemberValidationTest`: Tests all validation rules for the Member entity
   - No Spring context required
   - Fast execution

2. **Integration Tests** (10 tests)
   - `RemoteMemberRegistrationIT`: Tests member registration endpoint
   - `DuplicateEmailIT`: Tests duplicate email handling
   - `RemoteMemberRESTServiceIT`: Tests all GET endpoints
   - Full Spring Boot context with embedded Tomcat
   - Uses TestRestTemplate for HTTP calls

### Run Specific Test

```bash
# Run only unit tests
mvn test -Dtest=MemberValidationTest

# Run only integration tests
mvn test -Dtest="*IT"
```

### Test Coverage

```
Total Tests:    31
Unit Tests:     21 (validation rules)
Integration:    10 (REST API + Web UI)
Success Rate:   100%
```

## Database

### MongoDB Configuration

The application uses MongoDB as its database. Configuration is in `application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=kitchensink

# Auto-create indexes
spring.data.mongodb.auto-index-creation=true
```

### Sample Data

On startup, the `DataInitializer` component loads one sample member:

```java
Member johnSmith = new Member();
johnSmith.setName("John Smith");
johnSmith.setEmail("john.smith@mailinator.com");
johnSmith.setPhoneNumber("2125551234");
```

### MongoDB Collections

The application uses a single collection:

- **Collection Name**: `members`
- **Document Structure**:
  ```json
  {
    "_id": "ObjectId",
    "name": "String",
    "email": "String (unique index)",
    "phoneNumber": "String"
  }
  ```

### MongoDB Administration

#### Access MongoDB Shell

```bash
mongosh
use kitchensink
db.members.find().pretty()
```

#### View All Members

```bash
db.members.find()
```

#### Clear All Data

```bash
db.members.deleteMany({})
```

### MongoDB Atlas (Cloud)

To use MongoDB Atlas instead of local MongoDB, update `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/kitchensink?retryWrites=true&w=majority
```

### Testing with Embedded MongoDB

Tests use Flapdoodle embedded MongoDB, which automatically starts a MongoDB instance for testing. No separate MongoDB installation needed for tests.

## Project Structure

```
kitchensink/
├── src/
│   ├── main/
│   │   ├── java/org/quickstarts/kitchensink/
│   │   │   ├── KitchensinkApplication.java       # Spring Boot main class
│   │   │   ├── controller/
│   │   │   │   └── MemberController.java         # Web UI controller
│   │   │   ├── rest/
│   │   │   │   └── MemberResourceRESTService.java # REST API controller
│   │   │   ├── service/
│   │   │   │   └── MemberRegistration.java       # Business logic
│   │   │   ├── data/
│   │   │   │   └── MemberRepository.java         # Data access layer
│   │   │   └── model/
│   │   │       └── Member.java                   # JPA entity
│   │   └── resources/
│   │       ├── application.properties             # Configuration
│   │       ├── application.properties             # Configuration
│   │       └── templates/
│   │           └── index.html                     # Thymeleaf template
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java          # Sample data loader
│   └── test/
│       └── java/org/quickstarts/kitchensink/test/
│           ├── MemberValidationTest.java          # Unit tests
│           ├── RemoteMemberRegistrationIT.java    # Integration tests
│           ├── DuplicateEmailIT.java              # Integration tests
│           └── RemoteMemberRESTServiceIT.java     # Integration tests
├── pom.xml                                        # Maven configuration
└── README_SPRING_BOOT.md                          # This file
```

## Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=kitchensink
spring.data.mongodb.auto-index-creation=true

# Logging
logging.level.org.springframework=INFO
logging.level.org.springframework.data.mongodb=INFO
logging.level.org.quickstarts.kitchensink=INFO
```

### Customizing the Port

To run on a different port:

```bash
# Via command line
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090

# Or with JAR
java -jar target/kitchensink-8.0.0.GA.jar --server.port=9090
```

## Migration Notes

This application has undergone two major migrations:

### Migration 1: Jakarta EE/JBoss EAP → Spring Boot

| Jakarta EE | Spring Boot |
|------------|-------------|
| EJB @Stateless | @Service |
| CDI @Inject | @Autowired |
| JAX-RS | Spring MVC @RestController |
| JSF | Thymeleaf |
| EntityManager | MongoRepository |

### Migration 2: JPA/H2 → MongoDB

| JPA/H2 | MongoDB |
|--------|---------|
| @Entity | @Document |
| @Table | @Document(collection="...") |
| @Id Long | @Id String (ObjectId) |
| @Column | Direct field mapping |
| EntityManager | MongoRepository |
| JpaRepository | MongoRepository |
| SQL (import.sql) | DataInitializer component |
| Relational tables | Document collections |

### Package Structure

- **Old**: `org.jboss.as.quickstarts.kitchensink.*`
- **New**: `org.quickstarts.kitchensink.*`

### ID Field Changes

- **JPA**: `Long id` with `@GeneratedValue`
- **MongoDB**: `String id` with MongoDB ObjectId generation
- **REST API**: ID parameter changed from numeric to alphanumeric

### Benefits of Migrations

✅ Modern Spring Boot framework
✅ NoSQL flexibility with MongoDB
✅ Better scalability with document database
✅ Simplified configuration
✅ Embedded Tomcat server
✅ Better testing support with embedded MongoDB
✅ Cloud-ready (easy MongoDB Atlas integration)
✅ Larger Spring + MongoDB ecosystem

## Troubleshooting

### MongoDB Not Running

If you see connection errors like "MongoSocketOpenException":

```bash
# Check if MongoDB is running
mongosh --eval "db.version()"

# Start MongoDB
# macOS
brew services start mongodb-community

# Linux
sudo systemctl start mongod

# Check MongoDB status
brew services list  # macOS
sudo systemctl status mongod  # Linux
```

### Port Already in Use

If port 8080 is already in use:

```bash
# Check what's using the port (Mac/Linux)
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or use a different port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Build Failures

```bash
# Clean and rebuild
mvn clean install

# Clear Maven cache if needed
rm -rf ~/.m2/repository/org/quickstarts/kitchensink
mvn clean install
```

### Tests Failing

```bash
# Run tests with debug output
mvn test -X

# Skip tests temporarily
mvn package -DskipTests
```

## Contributing

When contributing to this project:

1. Ensure all tests pass: `mvn test`
2. Follow existing code style
3. Add tests for new features
4. Update this README if adding new endpoints or features

## License

This project is licensed under the Apache License 2.0. See the license header in source files for details.

## Support

For questions or issues:
- Check existing documentation
- Review test cases for examples
- Open an issue in the repository

---

**Note**: This is a demonstration application showing Spring Boot best practices. For production use, consider adding:
- Security (Spring Security)
- Monitoring (Spring Boot Actuator)
- Logging (SLF4J with Logback)
- External database configuration
- Caching (Spring Cache)
- API documentation (SpringDoc/Swagger)
