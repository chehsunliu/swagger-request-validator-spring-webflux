package io.github.chehsunliu.openapi.validator.tests.middleware;

import io.github.chehsunliu.openapi.validator.tests.middleware.model.ErrorResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    /** Refer to ResponseEntityExceptionHandler. */
    @ExceptionHandler({
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class,
        MissingServletRequestPartException.class,
        ServletRequestBindingException.class,
        MethodArgumentNotValidException.class,
        NoHandlerFoundException.class,
        NoResourceFoundException.class,
        AsyncRequestTimeoutException.class,
        ErrorResponseException.class,
        MaxUploadSizeExceededException.class,
        ConversionNotSupportedException.class,
        TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        HttpMessageNotWritableException.class,
        MethodValidationException.class,
        AsyncRequestNotUsableException.class
    })
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        var message = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseBody.of(400_000, message));
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Object> handleUnknownException(Exception ex) {
        var message = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName();
        log.atError()
                .addKeyValue("exception", ex.getClass().getName())
                .setCause(ex)
                .log(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseBody.of(400_000, message));
    }
}
