package com.linkagent.demo.connectors;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectorRegistry {
  private final Map<String, Connector> connectors = new ConcurrentHashMap<>();

  public void register(Connector c) {
    connectors.put(c.name().toLowerCase(Locale.ROOT), c);
  }

  public Optional<Connector> find(String name) {
    if (name == null) return Optional.empty();
    return Optional.ofNullable(connectors.get(name.toLowerCase(Locale.ROOT)));
  }

  public List<Map<String,String>> list() {
    return connectors.values().stream()
        .map(c -> Map.of("name", c.name()))
        .sorted(Comparator.comparing(m -> m.get("name")))
        .toList();
  }
}
