package com.linkagent.demo.web;

import com.linkagent.demo.connectors.ConnectorRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConnectorsController {
  private final ConnectorRegistry registry;
  public ConnectorsController(ConnectorRegistry registry) { this.registry = registry; }

  @GetMapping("/connectors")
  public ResponseEntity<Map<String,Object>> list() {
    return ResponseEntity.ok(Map.of("connectors", registry.list()));
  }
}
