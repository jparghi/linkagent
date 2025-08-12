package com.linkagent.demo.dto;

import java.util.Map;

public record ExecuteRequest(String intent, String target, String idempotencyKey, Map<String,Object> params) {}
