package io.github.chehsunliu.openapi.validator.tests.controller;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;
    private final OpenApiInteractionValidator validator = OpenApiInteractionValidator.createFor(
                    "/static/openapi/swagger.yaml")
            .build();

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .filter(new OpenApiValidationFilter(validator))
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

    @Test
    void echo() {
        @Language("json")
        var requestBody = """
{
  "key": "value"
}
""";

        this.client
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
