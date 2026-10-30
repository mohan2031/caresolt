# Care Solt Android on-device AI — Design Specification

**Date:** 2026-10-30  
**Status:** Draft for user review; no implementation approved yet  
**Target repository:** `mohan2031/caresolt`  
**Target branch:** `feat/care-solt-android-mvp`

## 1. Goal

Add an optional, native on-device AI assistant to the existing Care Solt Android application using Google's LiteRT-LM Kotlin API. The separate Google AI Edge Gallery app is a testing/reference app, not an embeddable dependency.

## 2. Existing project context

The existing Android app is a Kotlin + Jetpack Compose starter with Home, Bookings, Queue and Help tabs. Its booking and queue screens are currently placeholders; real authentication and workflow APIs are not implemented in this Android starter. The separate CareSolt-GPT repository provides the existing MCP endpoint and a read-only `search_hospitals` tool.

Keep the Android and CareSolt-GPT repositories separate. Reuse existing Care Solt services only through their existing public, approved interfaces.

## 3. User experience

- Add an AI Assistant entry point without replacing existing tabs or workflows.
- On first open, explain that an on-device model must be downloaded and show **Download** and **Skip** choices.
- Download only after the user explicitly chooses Download.
- Show progress, cancellation/retry states, available storage guidance, and a clear model-ready state.
- If skipped, unavailable, deleted, or unable to initialize, the normal app remains usable.
- Keep the assistant optional and controllable through a feature flag.

## 4. Runtime and model handling

- Use Google's LiteRT-LM Android Kotlin API after confirming a compatible stable dependency version.
- Store the model in app-private device storage; never bundle a large model in the APK or place it in assets.
- Verify downloaded file size/integrity against trusted model metadata before loading.
- Initialize and run the engine off the UI thread; manage engine/conversation lifecycle safely.
- Do not select a final model or claim device compatibility until target-device RAM, free storage, model license, download size, startup time, latency, thermal/battery behavior, and Kannada/English quality are evaluated.
- Model download is user-initiated and optional; no paid service or project is introduced.

## 5. Initial assistant scope

First prototype: reviewed, non-clinical Care Solt service FAQs and navigation guidance in Kannada and English. Clearly identify AI-generated answers and provide a staff-contact fallback.

Public hospital discovery may be added only after testing compatibility with the existing MCP endpoint. Initially expose only the existing read-only `search_hospitals` capability. Validate tool arguments and returned fields; show only actual returned public directory results; handle empty results and service errors distinctly. Booking remains in the existing Care Solt booking flow.

## 6. Privacy and safety boundaries

- No patient identifiers, medical records, prescriptions, appointment details, queue information, or other private data in model prompts or MCP calls.
- No diagnosis, prescribing, triage, emergency decision-making, or autonomous consequential actions.
- No service-role secrets or privileged credentials in the Android app.
- Treat model output and tool arguments as untrusted input.
- No additional MCP tools or write actions without a separate security/authentication design and explicit approval.

## 7. Non-goals and protected systems

This work must not:
- Rebuild Care Solt or create another Android project/repository.
- Create a new Supabase or Vercel project or any paid service.
- Replace or alter the existing backend, database, authentication, roles, RLS, schema, production environment, or deployment.
- Remove routes, features, or existing patient, doctor, reception, queue, or admin workflows.
- Add fake/demo production data or guest authentication.
- Deploy or publish without separate explicit approval.

## 8. Validation and acceptance criteria

1. Existing Android app builds before and after the feature work.
2. AI can be skipped or disabled without blocking normal app navigation.
3. Download cancellation, retry, insufficient storage, corrupt/incomplete model, missing model, and initialization failure are handled safely.
4. Model initialization and inference do not block the UI thread.
5. English and Kannada FAQ behavior is tested on the actual target device.
6. Device performance and resource use are measured and documented before choosing a default model.
7. MCP integration, if enabled, returns only real public hospital directory data from the existing `search_hospitals` tool; empty and error states are distinct.
8. No changes are made to production services, Supabase, authentication, RLS, database schema, or deployment.
9. No code implementation begins until this specification is reviewed and approved.

## 9. Implementation sequence after spec approval

1. Audit Android module, Gradle catalogs, manifest, package structure, and existing UI before editing.
2. Confirm target device model/Android version, available RAM and storage.
3. Verify LiteRT-LM dependency/version and build a minimal compile-tested runtime spike.
4. Implement optional download/skip and local FAQ prototype behind a feature flag.
5. Test performance and Kannada/English quality on-device.
6. Separately validate MCP Streamable HTTP compatibility, then add only the read-only public hospital search if the test passes.
7. Run build, unit/instrumentation checks available in the repository, and regression review.
8. Ask for explicit approval before any production deployment or release.
