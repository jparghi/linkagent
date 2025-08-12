package com.linkagent.demo.web;

import com.linkagent.demo.dto.ExecuteRequest;
import com.linkagent.demo.dto.ExecuteResponse;
import com.linkagent.demo.orchestrator.AgentOrchestrator;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentController {
  private final AgentOrchestrator orchestrator;
  public AgentController(AgentOrchestrator orchestrator) { this.orchestrator = orchestrator; }

  @PostMapping(
      value = "/execute",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ExecuteResponse> execute(@Valid @RequestBody ExecuteRequest req) {
    var res = orchestrator.execute(req.intent(), req.target(), req.idempotencyKey(), req.params());
    return ResponseEntity.ok(res);
  }
}
