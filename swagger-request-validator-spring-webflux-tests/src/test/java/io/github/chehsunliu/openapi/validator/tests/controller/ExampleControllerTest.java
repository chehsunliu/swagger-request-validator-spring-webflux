package io.github.chehsunliu.openapi.validator.tests.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import io.github.chehsunliu.openapi.validator.OpenApiValidationFilter;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;
    private WebClient webClient;
    private final OpenApiInteractionValidator validator = OpenApiInteractionValidator.createFor(
                    "/static/openapi/swagger.yaml")
            .build();

    @BeforeEach
    void setUp() {
        var filter = new OpenApiValidationFilter(validator);
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .filter(filter)
                .build();
        webClient = WebClient.builder()
                .baseUrl("http://localhost:%d".formatted(port))
                .filter(filter)
                .build();
    }

    @Test
    void getHealth() {
        var r = this.webClient
                .get()
                .uri("/api/v1/health")
                .retrieve()
                .toEntity(String.class)
                .block();

        assertNotNull(r);
        assertTrue(r.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getHealth_by_WebTestClient() {
        this.webTestClient
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

    @Test
    void echo() {
        @Language("json")
        var requestBody = """
{
  "key": "value"
}
""";

        var r = this.webClient
                .post()
                .uri("/api/v1/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertNotNull(r);
        assertTrue(r.getStatusCode().is2xxSuccessful());
    }

    @Test
    void echo_by_WebTestClient() {
        @Language("json")
        var requestBody = """
{
  "key": "value"
}
""";

        this.webTestClient
                .post()
                .uri("/api/v1/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("""
{
  "data": {
    "s": "{\\"key\\":\\"value\\"}"
  }
}
""", JsonCompareMode.STRICT);
    }
}
