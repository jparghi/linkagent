package com.linkagent.demo.idempotency;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface IdempotencyStore {
  Optional<Map<String,Object>> get(String key);
  Map<String,Object> putAndGet(String key, Supplier<Map<String,Object>> supplier);
}
