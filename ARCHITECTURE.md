# LinkAgent — Architecture (Public-safe Demo)

- Client/Chat -> REST API -> AgentOrchestrator -> ConnectorRegistry -> [Jira|ServiceNow (mock)]
- Idempotency: in-memory TTL cache with background cleanup
- Observability: Micrometer + Actuator (Prometheus)

**Non-goals here**: real vendor SDKs/secrets (kept in private core).
