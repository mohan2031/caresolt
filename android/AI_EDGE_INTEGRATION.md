# Optional Google AI Edge integration plan

## Scope
Use Google AI Edge Gallery as a reference for local model UX and evaluation only. Do not embed the Gallery showcase app as the Care Solt product.

## First candidate
A local, non-clinical FAQ helper for service navigation and appointment instructions. It must use reviewed FAQ content, clearly identify itself as AI-assisted, and provide a fallback to contact hospital staff.

## Required gates
- Confirm a model/runtime compatible with current Android devices and app licensing.
- Benchmark install/download size, memory, battery, latency, Kannada quality and offline behavior.
- Do not use local AI to diagnose, prescribe, triage emergencies or make clinical decisions.
- Do not give the model direct database credentials or unrestricted tools.
- If tools are introduced, call only narrow backend endpoints that enforce authentication, authorization, tenant isolation and audit logging.
- Treat model output and tool arguments as untrusted; validate and confirm any consequential action.
- Do not include patient-identifiable content in prompts by default.
- Feature-flag the module and allow disabling it without affecting booking, login or queue workflows.

## MCP
MCP is not required for the first Android release. If later needed, validate protocol/client compatibility and use authenticated HTTPS with scoped, server-verified tool permissions. Never expose privileged MCP tools directly to the mobile app.
