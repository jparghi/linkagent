package com.linkagent.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/nlp")
public class NlpController {
  @PostMapping("/parse")
  public ResponseEntity<Map<String,Object>> parse(@RequestBody Map<String,Object> body) {
    String text = String.valueOf(body.getOrDefault("text","")).toLowerCase(Locale.ROOT);
    String intent = "create_issue";
    String target = text.contains("servicenow") ? "servicenow" : "jira";
    Map<String,Object> params = new HashMap<>();
    if (text.contains("login")) params.put("summary","Broken login");
    if (text.contains("high priority") || text.contains("priority high")) params.put("priority","high");
    if (!params.containsKey("summary")) params.put("summary","Unspecified request");
    return ResponseEntity.ok(Map.of("intent", intent, "target", target, "params", params));
  }
}
