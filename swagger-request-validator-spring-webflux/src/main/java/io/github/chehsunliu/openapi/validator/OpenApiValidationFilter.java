package io.github.chehsunliu.openapi.validator;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.SimpleValidationReportFormat;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OpenApiValidationFilter implements ExchangeFilterFunction {
    private final OpenApiInteractionValidator validator;

    public OpenApiValidationFilter(OpenApiInteractionValidator validator) {
        this.validator = validator;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        var bodyInserter = new InterceptedBodyInserter(request);
        var finalRequest = ClientRequest.from(request).body(bodyInserter).build();

        return next.exchange(finalRequest).flatMap(response -> response.bodyToMono(byte[].class)
                .defaultIfEmpty(new byte[0])
                .flatMap(body -> {
                    var requestBody = bodyInserter.getOutputStream().toByteArray();

                    var atlassianRequest = createAtlassianRequest(finalRequest, requestBody);
                    var atlassianResponse = createAtlassianResponse(response, body);

                    var report = validator.validate(atlassianRequest, atlassianResponse);
                    if (report.hasErrors()) {
                        var message = SimpleValidationReportFormat.getInstance().apply(report);
                        return Mono.error(new RuntimeException(message));
                    }

                    Flux<DataBuffer> buffer = Flux.just(new DefaultDataBufferFactory().wrap(body));
                    return Mono.just(response.mutate().body(buffer).build());
                }));
    }

    private static Request createAtlassianRequest(ClientRequest request, byte[] body) {
        var uriComponents = UriComponentsBuilder.fromUri(request.url()).build();
        var builder = new SimpleRequest.Builder(request.method().name(), uriComponents.getPath()).withBody(body);
        request.headers().forEach(builder::withHeader);
        uriComponents.getQueryParams().forEach(builder::withQueryParam);
        return builder.build();
    }

    private static Response createAtlassianResponse(ClientResponse response, byte[] body) {
        var builder = new SimpleResponse.Builder(response.statusCode().value()).withBody(body);
        response.headers().asHttpHeaders().forEach(builder::withHeader);
        return builder.build();
    }
}
