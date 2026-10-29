# Care Solt AI integration plan

Status: architecture and safety plan only. No production integration is enabled by this document.

## Goal

Bring three separate pieces into one Care Solt experience without merging their repositories or replacing the existing backend:

1. `android/`: native Kotlin + Jetpack Compose patient app.
2. `caresolt.-gpt`: existing Vite/Vercel project and read-only MCP directory service.
3. Google AI Edge: evaluate LiteRT-LM for optional on-device language assistance.

## Current verified boundaries

- Android starter is a UI scaffold. Real sign-in, appointment APIs, queue APIs and AI runtime are not implemented.
- CareSolt-GPT exposes the existing `GET/POST /api/mcp` endpoint and one read-only `search_hospitals` tool.
- The MCP tool reads only active public hospital directory fields through the existing Supabase anon role and existing RLS. It does not expose patients, appointments, queue data, credentials or write actions.
- Keep the two repositories separate. Reuse the existing Care Solt production backend and Supabase project.
- Do not create a new Supabase/Vercel project, change production settings, modify schema/RLS/auth, or deploy as part of this work.

## Proposed architecture

- Android app owns the patient UI and authenticated session.
- On-device LiteRT-LM is an optional, feature-flagged FAQ/service-navigation helper. Start with reviewed static FAQs; do not use it for diagnosis, prescribing, triage or clinical decisions.
- CareSolt-GPT remains the separate MCP server. Its current public tool can support public hospital discovery only.
- For the first prototype, test the MCP endpoint from a compatible MCP client such as the Google AI Edge Gallery experimental Streamable HTTP MCP feature. This is a compatibility experiment, not proof that the Gallery app can be embedded as a library.
- If the native Android app later needs MCP, implement a dedicated Streamable HTTP client or a narrow app-facing backend adapter. Never expose service-role credentials or privileged tools in the mobile app.

## Phased implementation

### Phase 0 — compatibility and device evaluation
- Confirm the target Android device OS, RAM and available storage.
- Evaluate a small, appropriately licensed LiteRT-LM model on-device.
- Measure model download size, startup time, response latency, memory and battery use.
- Test Kannada and English service-navigation FAQs.
- Test the existing MCP endpoint with a compatible client using only `search_hospitals`.
- Record actual results; do not infer availability from a successful health check.

### Phase 1 — Android local FAQ prototype
- Add a separate optional AI module behind a feature flag.
- Use reviewed, non-clinical Care Solt FAQ content.
- Keep answers clearly labeled as AI-assisted and provide a staff-contact fallback.
- Keep patient identifiers and medical records out of prompts.
- If the model is unavailable or too large, the app must continue working normally.

### Phase 2 — public hospital discovery
- Only after compatibility tests, connect to the existing read-only `search_hospitals` tool.
- Show only returned directory records and clearly handle empty/error results.
- Keep booking on the existing `https://www.caresolt.in/book` flow.
- Do not add appointment, patient, queue or write tools without a separate authentication and authorization design.

### Phase 3 — authenticated patient workflows
- Audit the existing Care Solt authentication and API contracts before coding.
- Use the existing supported sign-in flow and backend endpoints.
- Preserve server-side role checks, tenant isolation and Supabase RLS.
- Add contract and regression tests for booking, reception, doctor, queue and admin workflows before any release.

## Acceptance criteria

- No production backend, Supabase, Auth, RLS, schema, environment variable or deployment changes.
- No new paid service or project.
- AI can be disabled without affecting login, booking, queue or help.
- No fabricated hospital, doctor, appointment or queue data.
- Kannada/English quality and target-device performance are measured, not assumed.
- Model output and tool arguments are treated as untrusted input.
- No clinical advice or autonomous consequential actions.

## Immediate next action

Run the Phase 0 device and MCP compatibility checks, then implement only the smallest passing local FAQ prototype in this Android feature branch.