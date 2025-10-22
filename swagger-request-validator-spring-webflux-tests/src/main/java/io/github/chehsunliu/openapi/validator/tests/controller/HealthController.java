package io.github.chehsunliu.openapi.validator.tests.controller;

import io.github.chehsunliu.openapi.validator.tests.controller.model.GetHealthResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/api/v1/health")
    public ResponseEntity<GetHealthResponseBody> getHealth() {
        return ResponseEntity.ok(new GetHealthResponseBody("ok"));
    }
}
