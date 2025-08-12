#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${BASE_URL:-http://localhost:8080}"
KEY="demo-$(date +%s%N)"

echo "Building…"
mvn -q -DskipTests clean package

echo "Starting app…"
( mvn -q spring-boot:run ) &
PID=$!
trap "kill $PID >/dev/null 2>&1 || true" EXIT

echo "Waiting for /health…"
for i in {1..60}; do
  if curl -fsS "$BASE_URL/health" >/dev/null; then
    echo "UP"; break
  fi
  sleep 1
done

echo "Connectors:"
curl -s "$BASE_URL/connectors" | jq . 2>/dev/null || curl -s "$BASE_URL/connectors"

echo "Execute once:"
curl -s -X POST "$BASE_URL/agent/execute" -H "Content-Type: application/json" -d "{"intent":"create_issue","target":"jira","idempotencyKey":"$KEY","params":{"summary":"Demo"}}" | jq . 2>/dev/null || true

echo "Execute replay:"
curl -s -X POST "$BASE_URL/agent/execute" -H "Content-Type: application/json" -d "{"intent":"create_issue","target":"jira","idempotencyKey":"$KEY","params":{"summary":"Demo"}}" | jq . 2>/dev/null || true

echo "Metrics:"
curl -s "$BASE_URL/actuator/prometheus" | head -n 10 || true
