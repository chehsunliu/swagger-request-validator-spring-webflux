package io.github.chehsunliu.openapi.validator.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.chehsunliu.openapi.validator.tests.controller.model.GetHealthResponseBody;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    private static final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/api/v1/health")
    public ResponseEntity<GetHealthResponseBody> getHealth() {
        return ResponseEntity.ok(new GetHealthResponseBody("ok"));
    }

    @SneakyThrows
    @PostMapping("/api/v1/echo")
    public ResponseEntity<Object> echo(@RequestBody Object body) {
        return ResponseEntity.ok(Map.of("s", mapper.writeValueAsString(body)));
    }
}
