# Swagger Request Validator - Spring WebFlux

[![Test](https://github.com/chehsunliu/swagger-request-validator-spring-webflux/actions/workflows/test.yml/badge.svg)](https://github.com/chehsunliu/swagger-request-validator-spring-webflux/actions/workflows/test.yml)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.chehsunliu.openapi/swagger-request-validator-spring-webflux)

A Java library help integrating [Swagger Request Validator](https://bitbucket.org/atlassian/swagger-request-validator)
with Spring WebClient and WebTestClient.

## Installation

Add this dependency for Maven:

```xml
<dependency>
    <groupId>io.github.chehsunliu</groupId>
    <artifactId>swagger-request-validator-spring-webflux</artifactId>
    <version>${version}</version>
</dependency>
```

Add this dependency for Gradle:

```kotlin
dependencies {
    implementation("io.github.chehsunliu:swagger-request-validator-spring-webflux:${version}")
}
```

## Usage

```java
import static org.junit.jupiter.api.Assertions.*;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import io.github.chehsunliu.openapi.validator.OpenApiValidationFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest {
    @LocalServerPort
    private int port;

    @Test
    void exampleForWebClient() {
        var validator = OpenApiInteractionValidator.createFor("/openapi/swagger.yaml").build();
        var filter = new OpenApiValidationFilter(validator);
        
        var client = WebClient.builder()
                .baseUrl("http://localhost:%d".formatted(port))
                .filter(filter)
                .build();

        var r = client
                .get()
                .uri("/api/v1/health")
                .retrieve()
                .toEntity(String.class)
                .block();

        assertNotNull(r);
        assertTrue(r.getStatusCode().is2xxSuccessful());
    }

    @Test
    void exampleForWebTestClient() {
        var validator = OpenApiInteractionValidator.createFor("/static/openapi/swagger.yaml").build();
        var filter = new OpenApiValidationFilter(validator);

        var client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .filter(filter)
                .build();

        client
                .get()
                .uri("/api/v1/health")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("""
{
  "data": {
    "status": "ok"
  }
}
""", JsonCompareMode.STRICT);
    }
}
```
