package com.linkagent.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

public record ExecuteRequest(
    @NotBlank @Size(max = 64)  String intent,
    @NotBlank @Size(max = 64)  String target,
    @NotBlank @Size(max = 128) String idempotencyKey,
    Map<String,Object> params
) {}
