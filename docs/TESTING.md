# Testing

## Unit
mvn -q -DskipITs test

## Sanity (local)
make sanity

## Manual OK then replay
KEY="demo-$$"
curl -s -X POST localhost:8080/agent/execute -H 'Content-Type: application/json'   -d "{"intent":"create_issue","target":"jira","idempotencyKey":"$KEY","params":{"summary":"Demo"}}"
curl -s -X POST localhost:8080/agent/execute -H 'Content-Type: application/json'   -d "{"intent":"create_issue","target":"jira","idempotencyKey":"$KEY","params":{"summary":"Demo"}}"

## Basic perf (ApacheBench)
ab -n 200 -c 20 http://localhost:8080/health
