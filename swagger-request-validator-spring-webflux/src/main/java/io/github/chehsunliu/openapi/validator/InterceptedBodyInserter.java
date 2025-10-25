package io.github.chehsunliu.openapi.validator;

import java.io.ByteArrayOutputStream;
import lombok.Getter;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

class InterceptedBodyInserter implements BodyInserter<Object, ClientHttpRequest> {
    private final ClientRequest originalRequest;

    @Getter
    private final ByteArrayOutputStream outputStream;

    public InterceptedBodyInserter(ClientRequest originalRequest) {
        this.originalRequest = originalRequest;
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public Mono<Void> insert(ClientHttpRequest outputMessage, Context context) {
        outputMessage = new InterceptedClientHttpRequest(outputMessage, outputStream);
        return originalRequest.body().insert(outputMessage, context);
    }
}
