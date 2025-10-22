package io.github.chehsunliu.openapi.validator.tests.middleware.model;

public record ErrorResponseBody(Error error) {
    public static ErrorResponseBody of(int code, String message) {
        return new ErrorResponseBody(new Error(code, message, null));
    }

    public static ErrorResponseBody of(int code, String message, Object detail) {
        return new ErrorResponseBody(new Error(code, message, detail));
    }

    public record Error(int code, String message, Object detail) {}
}
