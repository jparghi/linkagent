# LinkAgent  
**Your AI bridge between chat commands and enterprise services**

[![CI](https://github.com/YOUR_GITHUB_USERNAME/linkagent/actions/workflows/ci.yml/badge.svg)](https://github.com/YOUR_GITHUB_USERNAME/linkagent/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)

---

**LinkAgent** is an **AI-powered connector framework** that lets users trigger service actions (e.g., creating Jira tickets, ServiceNow incidents) via **natural language**.

> Example: _"Hey LinkAgent, create a Jira ticket for the broken login form with high priority."_

### 🚀 Features
- Natural Language to Action (stubbed NLP -> JSON -> execute)
- Connector framework (mock Jira/ServiceNow) with clean SPI
- Idempotency handling (in-memory, public-safe)
- Observability via `/actuator/prometheus`
- One-click sanity (`make sanity`)

---

## 🏁 Quick Start
```bash
mvn -q -DskipTests clean package
mvn spring-boot:run
```

### Endpoints
```bash
# Health
curl -s http://localhost:8080/health

# Connectors
curl -s http://localhost:8080/connectors

# Execute
KEY="demo-$$"
curl -s -X POST http://localhost:8080/agent/execute   -H "Content-Type: application/json"   -d "{"intent":"create_issue","target":"jira","idempotencyKey":"$KEY","params":{"summary":"Demo ticket"}}"

# Replay (idempotent)
curl -s -X POST http://localhost:8080/agent/execute   -H "Content-Type: application/json"   -d "{"intent":"create_issue","target":"jira","idempotencyKey":"$KEY","params":{"summary":"Demo ticket"}}"

# Metrics
curl -s http://localhost:8080/actuator/prometheus | head
```

## Docs
See `ARCHITECTURE.md`, `DEMO.md`, and `openapi/openapi.yaml`.
