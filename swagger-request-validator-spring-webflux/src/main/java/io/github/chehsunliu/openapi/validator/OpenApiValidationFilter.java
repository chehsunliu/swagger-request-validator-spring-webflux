package io.github.chehsunliu.openapi.validator;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class OpenApiValidationFilter implements ExchangeFilterFunction {
    private final OpenApiInteractionValidator validator;

    public OpenApiValidationFilter(OpenApiInteractionValidator validator) {
        this.validator = validator;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request);
    }
}
