# Testing Strategy for Git Microservices Project

## 1. Purpose

Ensure the stability, quality, and uninterrupted operation of the microservices by covering different levels of testing.

## 2. Types of Testing & Approach

### Unit Tests
- **Tools:** JUnit 5 (with Mockito for mocking if needed)
- **Goal:** Cover business logic of individual classes (e.g., services).
- **Target:** ~80–90% coverage.

### Integration Tests
- **Tools:** JUnit 5 + Spring Boot Test + Testcontainers
- **Goal:** Verify interactions between layers (e.g., service ↔ repository, or service ↔ database).

### Component Tests
- **Tools:** Cucumber + Spring
- **Goal:** Test functional scenarios (e.g., MP3 upload, metadata saving).
- **Approach:** Describe scenarios in English (Gherkin).

### Contract Tests
- **Tools:** Spring Cloud Contract (or Pact)
- **Goal:** Ensure services comply with contracts during synchronous (REST) and asynchronous (RabbitMQ) communication.
- **Approach:** Write tests on both consumer and provider sides.

### End-to-End Tests
- **Tools:** Cucumber + RestAssured
- **Goal:** Test full flow — from MP3 upload to data persistence across all services.

## 3. Test Coverage Strategy

- Unit Tests — 80–90% coverage of business logic
- Integration Tests — 70% of critical scenarios
- Component Tests — main business functions
- Contract Tests — all services communicating via HTTP/RabbitMQ
- End-to-End Tests — critical user flows