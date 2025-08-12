package com.linkagent.demo.connectors;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceNowConnector implements Connector {

  private final ConnectorRegistry registry;

  public ServiceNowConnector(ConnectorRegistry registry) { this.registry = registry; }

  @PostConstruct
  public void init() { registry.register(this); }

  @Override public String name() { return "servicenow"; }

  @Override public boolean supportsIntent(String intent) { return "create_issue".equalsIgnoreCase(intent); }

  @Override public Map<String,Object> execute(String intent, Map<String,Object> params) {
    Map<String,Object> out = new HashMap<>();
    out.put("summary", String.valueOf(params.getOrDefault("summary","Demo from LinkAgent")));
    out.put("number", "INC0012345");
    out.put("status", "CREATED");
    return out;
  }
}
