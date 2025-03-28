package org.addy.customerservice.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    private static final String LOG_MSG_TEMPLATE = "An exception of type {} was raised while requesting {} {}";

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDeniedException(AccessDeniedException e) {
        reportError(e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNoSuchElementException(NoSuchElementException e) {
        reportError(e);

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        reportError(e);

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        reportError(e);

        return ResponseEntity.badRequest().body(getErrorMap(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        reportError(e);

        return ResponseEntity.internalServerError()
                .body("Something went wrong. Check the logs for details");
    }

    @NonNull
    private static Map<String, Object> getErrorMap(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        List<String> globalErrors = allErrors.stream()
                .filter(error -> !(error instanceof FieldError))
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map<String, Object> errors = allErrors.stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .collect(Collectors.toMap(FieldError::getField, error -> Objects.requireNonNullElse(
                        error.getDefaultMessage(),
                        String.format("Value '%s' was rejected", error.getRejectedValue()))));

        if (!globalErrors.isEmpty()) {
            errors.put("*", globalErrors);
        }

        return errors;
    }

    private void reportError(Exception e) {
        HttpServletRequest currentRequest = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        String requestURI = ServletUriComponentsBuilder.fromRequest(currentRequest).build().toUriString();

        log.error(LOG_MSG_TEMPLATE, e.getClass().getSimpleName(), currentRequest.getMethod(), requestURI, e);
    }
}
