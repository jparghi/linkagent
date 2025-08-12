package com.linkagent.demo.idempotency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Component
public class InMemoryIdempotencyStore implements IdempotencyStore {

  static class Entry {
    final Map<String,Object> value;
    final long ts;
    Entry(Map<String,Object> value) {
      this.value = value; this.ts = System.currentTimeMillis();
    }
  }

  private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();
  private final long ttlMillis;

  public InMemoryIdempotencyStore(@Value("${linkagent.idem.ttlMillis:3600000}") long ttlMillis) {
    this.ttlMillis = ttlMillis;
    Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "idem-cleaner"); t.setDaemon(true); return t;
    }).scheduleAtFixedRate(this::cleanup, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
  }

  private void cleanup() {
    long cutoff = System.currentTimeMillis() - ttlMillis;
    store.entrySet().removeIf(e -> e.getValue().ts < cutoff);
  }

  @Override public Optional<Map<String,Object>> get(String key) {
    Entry e = store.get(key);
    if (e == null) return Optional.empty();
    if (System.currentTimeMillis() - e.ts > ttlMillis) { store.remove(key); return Optional.empty(); }
    return Optional.of(e.value);
  }

  @Override public Map<String,Object> putAndGet(String key, Supplier<Map<String,Object>> supplier) {
    return store.computeIfAbsent(key, k -> new Entry(supplier.get())).value;
  }
}
