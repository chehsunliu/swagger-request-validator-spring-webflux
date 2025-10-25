package io.github.chehsunliu.openapi.validator;

import java.io.ByteArrayOutputStream;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class InterceptedClientHttpRequest extends ClientHttpRequestDecorator {
    private final ByteArrayOutputStream outputStream;

    public InterceptedClientHttpRequest(ClientHttpRequest delegate, ByteArrayOutputStream outputStream) {
        super(delegate);
        this.outputStream = outputStream;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        body = Flux.from(body).map(originalBuffer -> {
            var bytes = new byte[originalBuffer.readableByteCount()];

            originalBuffer.read(bytes);
            outputStream.write(bytes, 0, bytes.length);
            DataBufferUtils.release(originalBuffer);

            return getDelegate().bufferFactory().wrap(bytes);
        });
        return super.writeWith(body);
    }
}
