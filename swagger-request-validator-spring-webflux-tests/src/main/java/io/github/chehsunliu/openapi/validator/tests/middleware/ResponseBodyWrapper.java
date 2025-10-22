package io.github.chehsunliu.openapi.validator.tests.middleware;

import io.github.chehsunliu.openapi.validator.tests.middleware.model.ErrorResponseBody;
import io.github.chehsunliu.openapi.validator.tests.middleware.model.SuccessResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseBodyWrapper implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !converterType.equals(StringHttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof SuccessResponseBody || body instanceof ErrorResponseBody) {
            return body;
        }

        var status = HttpStatus.resolve(
                ((ServletServerHttpResponse) response).getServletResponse().getStatus());
        if (status == null) {
            throw new IllegalStateException("Status should not be null");
        }

        if (!status.is2xxSuccessful()) {
            throw new IllegalStateException("Unhandled error response");
        }

        return new SuccessResponseBody(body);
    }
}
