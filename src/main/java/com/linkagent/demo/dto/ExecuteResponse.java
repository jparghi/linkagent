package com.linkagent.demo.dto;

import java.time.Instant;
import java.util.Map;

public record ExecuteResponse(String status, String message, String correlationId, Instant timestamp, Map<String,Object> data, boolean replay) {}
