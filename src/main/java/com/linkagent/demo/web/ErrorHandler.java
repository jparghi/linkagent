package com.linkagent.demo.web;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> badReq(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
        "error","BAD_REQUEST",
        "message", ex.getMessage(),
        "correlationId", MDC.get("corrId"),
        "timestamp", Instant.now().toString()
    ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> oops(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
        "error","CONNECTOR_FAILURE",
        "message", ex.getMessage(),
        "correlationId", MDC.get("corrId"),
        "timestamp", Instant.now().toString()
    ));
  }
}
