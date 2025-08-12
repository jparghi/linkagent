# UAT Checklist

- ✅ Health, Connectors, Execute work as expected
- ✅ Idempotent replay on same key
- ✅ 400 on missing/blank fields; 415 on wrong content-type
- ✅ 429 when bursting POST /agent/execute beyond limit
- ✅ Security headers present; no server stack traces leaked
- ✅ Metrics endpoint responds (/actuator/prometheus)
- ✅ README “Quick Start” works on a clean machine
