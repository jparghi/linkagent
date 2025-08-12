package com.linkagent.demo.orchestrator;

import com.linkagent.demo.connectors.Connector;
import com.linkagent.demo.connectors.ConnectorRegistry;
import com.linkagent.demo.dto.ExecuteResponse;
import com.linkagent.demo.idempotency.IdempotencyStore;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgentOrchestrator {

  private final ConnectorRegistry registry;
  private final IdempotencyStore idem;

  public AgentOrchestrator(ConnectorRegistry registry, IdempotencyStore idem) {
    this.registry = registry; this.idem = idem;
  }

  public ExecuteResponse execute(String intent, String target, String key, Map<String,Object> params) {
    String corr = Optional.ofNullable(MDC.get("corrId")).orElse(UUID.randomUUID().toString());

    if (intent == null || intent.isBlank()) throw new IllegalArgumentException("intent is required");
    if (target == null || target.isBlank()) throw new IllegalArgumentException("target is required");
    if (key == null || key.isBlank()) throw new IllegalArgumentException("idempotencyKey is required");

    var cached = idem.get(key);
    if (cached.isPresent()) {
      return new ExecuteResponse("IDEMPOTENT_REPLAY", "Idempotent replay", corr, Instant.now(), cached.get(), true);
    }

    Connector c = registry.find(target).orElseThrow(() -> new IllegalArgumentException("Unknown target: " + target));
    if (!c.supportsIntent(intent)) throw new IllegalArgumentException("Unsupported intent for target: " + intent);

    Map<String,Object> data = idem.putAndGet(key, () -> c.execute(intent, params));
    return new ExecuteResponse("OK", "Executed", corr, Instant.now(), data, false);
  }
}
