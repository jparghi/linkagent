# Security Guidelines (Public Demo)

- No secrets in code or repo.
- All write endpoints require `Content-Type: application/json`.
- Idempotency key is required; we log correlation IDs only.
- Rate limiting: simple per‑IP sliding window (demo-level).
- CORS: disabled by default for public demo.

Report issues: create a GitHub Issue with minimal repro; omit secrets.
