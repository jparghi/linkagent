package com.linkagent.demo.web;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandler {

  private Map<String, Object> base(String code, String message) {
    Map<String,Object> m = new HashMap<>();
    m.put("error", code);
    m.put("message", message);
    m.put("correlationId", MDC.get("corrId"));
    m.put("timestamp", Instant.now().toString());
    return m;
  }

  /** 400: bean validation on @RequestBody (@Valid) */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex) {
    var details = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .collect(Collectors.toList());
    Map<String,Object> body = base("VALIDATION_FAILED", "Request validation failed");
    body.put("details", details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  /** 400: malformed JSON / unreadable request */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String,Object>> handleUnreadable(HttpMessageNotReadableException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("BAD_REQUEST", "Malformed JSON or unreadable request body"));
  }

  /** 415: wrong content-type */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Map<String,Object>> handleUnsupportedType(HttpMediaTypeNotSupportedException ex) {
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(base("UNSUPPORTED_MEDIA_TYPE", "Content-Type must be application/json"));
  }

  /** 400: explicit bad input from our code paths */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> handleBadInput(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(base("BAD_REQUEST", ex.getMessage()));
  }

  /** 502: anything else bubbling up from downstream connectors, etc. */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> handleGeneric(Exception ex) {
    // You can log ex here at ERROR
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(base("CONNECTOR_FAILURE", ex.getMessage()));
  }
}