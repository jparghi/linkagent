# Patch Notes for LA-5…LA-8 (Drop-in)

1) Copy the contents of this zip over your repo root (it only adds/overwrites a few files):
   - docs/SECURITY.md, docs/TESTING.md, docs/UAT_CHECKLIST.md
   - src/main/java/com/linkagent/demo/dto/ExecuteRequest.java
   - src/main/java/com/linkagent/demo/web/AgentController.java
   - src/main/java/com/linkagent/demo/web/SecurityHeadersFilter.java
   - src/main/java/com/linkagent/demo/web/RateLimitFilter.java

2) Ensure you have validation starter in pom.xml (add if missing):
   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   ```

3) Run locally to verify:
   ```bash
   make sanity
   # burst requests to see 429s
   for i in $(seq 1 80); do
     curl -s -o /dev/null -w "%{http_code}\n"        -X POST localhost:8080/agent/execute        -H 'Content-Type: application/json'        -d '{"intent":"create_issue","target":"jira","idempotencyKey":"'$i'","params":{"summary":"x"}}'
   done | sort | uniq -c
   # expect some 429 once limit exceeded (default 60/min)
   ```

4) Open your PR “Hardening: Docs + Security + Perf + UAT” and reference LA‑5, LA‑6, LA‑7, LA‑8.
