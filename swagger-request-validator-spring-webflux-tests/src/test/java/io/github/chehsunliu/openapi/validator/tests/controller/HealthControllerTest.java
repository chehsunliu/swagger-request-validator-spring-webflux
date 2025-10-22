package io.github.chehsunliu.openapi.validator.tests.controller;

import io.github.chehsunliu.openapi.validator.OpenApiValidationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .filter(new OpenApiValidationFilter(null))
                .build();
    }

    @Test
    void getHealth() {
        this.client
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
