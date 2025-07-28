# kitchensink-spring-boot

A modern **Spring Boot 3** re-implementation of the classic JBoss EAP *kitchensink* sample application.  
The goal of this module is to demonstrate how a legacy Jakarta EE (JBoss EAP) application can be migrated to Spring Boot 3 / Java 21 while preserving functionality and improving developer experience.

---

## 1. Project Description

The application manages a simple list of *Members*:
* register a member  
* list all members (ordered by name)  
* look up a member by `id`

This repository contains **only Phase 2 – Core Application Migration**: domain model, persistence, business logic, REST API, configuration, validation, global exception handling and tests.

---

## 2. Migration Highlights

| Concern | JBoss EAP 8 (original) | Spring Boot 3 (this project) |
|---------|------------------------|------------------------------|
| Dependency management | Multi-module Maven BOMs | Single Spring Boot starter parent |
| Dependency injection | CDI (`@Inject`, beans.xml) | Spring DI / `@Autowired` |
| Persistence | JPA, programmatic `EntityManager` & Criteria API | Spring Data JPA `JpaRepository` |
| Business logic | EJB `@Stateless` | `@Service` + `@Transactional` |
| REST API | JAX-RS (`@Path`, `@GET`, …) | Spring MVC (`@RestController`, `@GetMapping`, …) |
| Validation | Jakarta Bean Validation | Spring Boot starter-validation (same annotations) |
| Exception handling | Per-resource `try/catch`, no global handler | `@ControllerAdvice` global handler |
| Configuration | persistence.xml, standalone.xml, CLI | `application.properties`, auto-config |
| DB | H2 (datasource) | Embedded H2, auto-migrated via `import.sql` |
| Tests | Arquillian container IT | Spring Boot Test + TestRestTemplate |

The resulting jar is **self-contained**, requiring only a JVM to run (no application server).

---

## 3. Technology Stack

* Java 21  
* Spring Boot 3.2.1
  * spring-boot-starter-web
  * spring-boot-starter-data-jpa
  * spring-boot-starter-validation
* H2 in-memory database (`spring.datasource.url=jdbc:h2:mem:kitchensinkdb`)
* Lombok 1.18.x (optional, compile-only)
* Maven 3.9+
* JUnit 5 / Spring Boot Test

---

## 4. Build Instructions

```bash
# From the module root
mvn clean package
```

The command:

* runs the unit/integration test suite
* produces `target/kitchensink-spring-boot-8.0.0.GA.jar`

---

## 5. Running the Application

### 5.1 Local execution

```bash
# Build & run in one step
mvn spring-boot:run
```

or

```bash
java -jar target/kitchensink-spring-boot-8.0.0.GA.jar
```

*Default URL*: `http://localhost:8080/kitchensink`

### 5.2 H2 Console

Enabled at `http://localhost:8080/kitchensink/h2-console`  
JDBC URL: `jdbc:h2:mem:kitchensinkdb` (username `sa`, empty password).

---

## 6. REST API Reference

Base path: `/kitchensink/api/members`

| Method | Endpoint | Description | Responses |
|--------|----------|-------------|-----------|
| `GET`  | `/` | List all members (ordered by name) | `200 OK` → `Member[]` |
| `GET`  | `/{id}` | Get member by id | `200 OK` → `Member` &nbsp;•&nbsp; `404 NOT_FOUND` |
| `POST` | `/` | Create a new member | `200 OK` → persisted `Member`<br>`400 BAD_REQUEST` → validation errors<br>`409 CONFLICT` → duplicate email |

### 6.1 Data Model

```jsonc
{
  "id": 1,                // generated, Long
  "name": "Jane Doe",     // String, 1-25 chars, letters only
  "email": "jane@mail.com",      // valid email, unique
  "phoneNumber": "2125551212"    // 10-12 digits
}
```

### 6.2 Example – Create Member

```bash
curl -X POST http://localhost:8080/kitchensink/api/members \
     -H "Content-Type: application/json" \
     -d '{"name":"Test User","email":"test.user@example.com","phoneNumber":"2125559999"}'
```

Response `200 OK`:

```json
{
  "id": 5,
  "name": "Test User",
  "email": "test.user@example.com",
  "phoneNumber": "2125559999"
}
```

---

## 7. Testing Instructions

Run the full suite:

```bash
mvn test
```

Coverage:

1. **Repository layer** – CRUD operations, custom queries.  
2. **Service layer** – business logic, email uniqueness, transactions.  
3. **REST layer** – all HTTP endpoints incl. error scenarios (validation, duplicate email).  
4. **Global Exception Handler** – verifies correct status codes & payloads.  

Tests use the real H2 DB with data seeded from `import.sql`, launched on a random port via `SpringBootTest(webEnvironment = RANDOM_PORT)`.

---

## 8. Migration Notes

* **One-click build**  
  *Original*: required JBoss EAP runtime & wildfly-maven-plugin.  
  *Spring Boot*: single jar, embedded Tomcat.

* **Transactions**  
  *Original*: container-managed (EJB).  
  *Spring Boot*: `@Transactional` with Spring’s transaction manager.

* **Events**  
  *Original*: CDI `Event<Member>.fire(member)`.  
  *Spring Boot*: `ApplicationEventPublisher.publishEvent(member)`.

* **Criteria vs Repository**  
  *Original*: manual `CriteriaBuilder` queries.  
  *Spring Boot*: derived query methods `findAllByOrderByNameAsc()`.

* **Validation / Exception Handling**  
  Moved from per-endpoint `try/catch` to global `@ControllerAdvice`.

* **Configuration**  
  `application.properties` replaces `persistence.xml`, `standalone.xml`, datasource modules.

* **Deployment**  
  Works on any environment with JDK 21 (`java -jar …`), can be containerized easily:

  ```Dockerfile
  FROM eclipse-temurin:21-jre
  COPY target/kitchensink-spring-boot-*.jar app.jar
  ENTRYPOINT ["java","-jar","/app.jar"]
  ```

---

## 9. Contributing

PRs & issue reports are welcome. Please ensure:

1. New code follows the existing style (`@Service`, Lombok `@Data`, etc.).  
2. Tests are added/updated.  
3. `mvn clean verify` passes.

---

## 10. License

Apache License 2.0 – see [`LICENSE`](../../LICENSE) for details.
