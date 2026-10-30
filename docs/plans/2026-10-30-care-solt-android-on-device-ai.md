# Care Solt Android On-Device AI Implementation Plan

> **For agentic workers:** Use the host's available task-by-task implementation workflow. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add an optional bilingual on-device FAQ assistant to the existing Care Solt Android app using LiteRT-LM, then separately enable read-only public hospital discovery if MCP compatibility is proven.

**Architecture:** Keep the feature in the existing Kotlin + Jetpack Compose Android app and behind a disabled-by-default feature flag. Download a compatible model only after user consent, store it in app-private storage, and run inference off the UI thread. Keep the existing CareSolt-GPT MCP service separate; add only its existing public `search_hospitals` tool after a dedicated compatibility and safety test.

**Tech Stack:** Kotlin, Android Gradle Plugin 8.7.3, Kotlin 2.0.21, Jetpack Compose Material 3, AndroidX Lifecycle, Google LiteRT-LM Android Kotlin API, Kotlin coroutines/Flow, JUnit and Android instrumentation tests.

## Global Constraints

- Work only in the existing `mohan2031/caresolt` repository and `feat/care-solt-android-mvp` branch.
- Do not rebuild Care Solt or create another Android project/repository.
- Do not create a new Supabase or Vercel project or any paid service.
- Do not replace or alter the existing backend, database, authentication, roles, RLS, schema, production environment, or deployment.
- Do not remove routes, features, or existing patient, doctor, reception, queue, or admin workflows.
- Do not add fake/demo production data or guest authentication.
- Do not deploy or publish without separate explicit approval.
- AI must remain optional. Skipping, disabling, deleting the model, or failing to initialize it must not block normal app navigation.
- Do not send patient identifiers, medical records, prescriptions, appointment details, queue information, or other private data to the model or MCP.
- Do not provide diagnosis, prescribing, triage, emergency decision-making, or autonomous consequential actions.
- Never put service-role secrets or privileged credentials in the Android app.
- Treat model output and tool arguments as untrusted input.
- Use only actual public directory results from the existing read-only `search_hospitals` MCP tool; distinguish empty results from transport/server errors.
- Do not choose a final model or claim device compatibility until model license, file size, target-device resources, startup time, latency, thermal/battery behavior, and Kannada/English quality are measured.

---

## Repository observations

- Observed Android entry point: `android/app/src/main/java/in/caresolt/mobile/MainActivity.kt`.
- Observed app module build file: `android/app/build.gradle.kts`; dependencies are currently declared directly, with no `android/gradle/libs.versions.toml` present at the inspected branch.
- Observed root Android Gradle files: `android/build.gradle.kts` and `android/settings.gradle.kts`.
- Observed current UI: Home, Bookings, Queue and Help tabs; language toggle; booking and queue are explicitly placeholders; Help contains static bilingual service FAQs.
- Observed Android README says JDK 17 and Android SDK 35 are required and notes that a Gradle wrapper may need to be generated.
- Proposed paths below are new files unless marked Modify. Confirm package/source-set conventions and test dependencies during Task 1 before creating them.

## Task 1: Establish baseline and add an isolated AI feature shell

**Files:**
- Modify: `android/app/src/main/java/in/caresolt/mobile/MainActivity.kt` — add an AI Assistant entry point without removing or reordering existing tabs.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/AiFeatureConfig.kt` — feature flag and assistant availability state.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/AiAssistantScreen.kt` — Compose UI for disabled, not-downloaded, loading, ready, and unavailable states.
- Test (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/AiFeatureConfigTest.kt`.
- Test (proposed): `android/app/src/androidTest/java/in/caresolt/mobile/ai/AiAssistantNavigationTest.kt`.

**Interfaces:**
- Consumes: existing `CareSoltApp()`, `AppTab`, language toggle state, and Compose Material 3.
- Produces: an isolated assistant screen and feature-availability state; existing app tabs remain reachable regardless of AI state.

- [ ] **Step 1: Add focused failing tests**

Test that the feature is disabled by default; the assistant can be opened when enabled; existing Home, Bookings, Queue and Help navigation remains available; and a disabled assistant does not alter normal app content.

- [ ] **Step 2: Verify the relevant failure**

Run the repository's Android unit and instrumentation test tasks after confirming whether a Gradle wrapper exists. Expected: the new test sources initially fail to compile or the new behavior assertions fail because the AI feature shell is not yet present. Record the exact Gradle command and output in the PR.

- [ ] **Step 3: Implement the minimum behavior**

Add an AI entry point and a separate Compose screen. Keep the feature flag false by default. Do not add the LiteRT-LM dependency or network/model code in this task. Preserve all existing tab labels, navigation destinations, language behavior, and placeholder safety text.

- [ ] **Step 4: Verify the focused pass**

Run the focused unit and Compose navigation tests. Expected: feature disabled by default, AI screen only reachable when enabled, and all four existing tabs remain reachable.

- [ ] **Step 5: Run the affected integration check**

Run the Android debug assemble task. Expected: app compiles with the feature flag disabled and existing screens unchanged.

- [ ] **Step 6: Commit the passing deliverable**

```bash
git add android/app/src/main
git add android/app/src/test android/app/src/androidTest
git commit -m "feat(android): add optional AI assistant shell"
```

## Task 2: Validate LiteRT-LM and implement consent-based local model lifecycle

**Files:**
- Modify: `android/app/build.gradle.kts` — add only a verified compatible LiteRT-LM dependency and test dependencies.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/ModelDownloadCoordinator.kt` — explicit user-consented download, progress, cancellation, retry, integrity check, and app-private storage.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/LiteRtModelRuntime.kt` — runtime initialization, inference dispatcher, and lifecycle cleanup.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/AiModelState.kt` — typed model states and events.
- Create (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/ModelDownloadCoordinatorTest.kt`.
- Create (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/AiModelStateTest.kt`.

**Interfaces:**
- Consumes: Task 1 assistant shell; verified official LiteRT-LM Android artifact/API; trusted model metadata and model source selected only after license and device evaluation.
- Produces: observable states `NotDownloaded`, `AwaitingConsent`, `Downloading(progress)`, `Ready`, `Cancelled`, and `Unavailable(reason)`; a runtime interface that exposes asynchronous initialization and text generation without exposing model internals to Compose UI.

- [ ] **Step 1: Add focused failing tests**

Cover no download before consent; Skip causes no network request; download progress; cancellation removes incomplete temporary files; retry after network failure; insufficient storage; checksum/size mismatch; model file missing after previously ready; runtime initialization failure; and inference dispatched away from the main thread.

- [ ] **Step 2: Verify the relevant failure**

Run focused unit tests. Expected: tests fail because the coordinator and runtime lifecycle are not implemented.

- [ ] **Step 3: Implement the minimum behavior**

First confirm a stable LiteRT-LM Android artifact and exact Kotlin API from Google's current official documentation. Do not use a dynamic or unverified dependency version. Evaluate model license, download size and device compatibility before selecting a model. Show a consent explanation with Download and Skip. Download only after Download, write to a temporary app-private file, verify trusted size and cryptographic digest, then atomically move to the final app-private model location. Never bundle the model in the APK. Initialize/infer on a background dispatcher and expose progress/state through Flow or an equivalent lifecycle-aware observable interface. If no suitable model passes evaluation, keep the feature unavailable and the normal app fully usable.

- [ ] **Step 4: Verify the focused pass**

Run the lifecycle unit tests. Expected: all consent, skip, cancellation, retry, integrity, storage, missing-file, initialization-failure and threading cases pass.

- [ ] **Step 5: Run the affected integration check**

Run Android debug assemble and an emulator/device smoke test. Expected: app starts without a model; Skip returns to normal app; Download shows progress; invalid/incomplete model is never loaded; model initialization does not freeze the UI.

- [ ] **Step 6: Commit the passing deliverable**

```bash
git add android/app/build.gradle.kts android/app/src/main android/app/src/test
git commit -m "feat(android): add optional on-device model lifecycle"
```

## Task 3: Add bilingual non-clinical FAQ conversations

**Files:**
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/CareSoltFaqRepository.kt` — reviewed, versioned, non-clinical FAQ content.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/AiConversationViewModel.kt` — conversation state and runtime calls.
- Modify (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/AiAssistantScreen.kt` — chat UI, loading/error states and staff fallback.
- Create (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/CareSoltFaqRepositoryTest.kt`.
- Create (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/AiConversationViewModelTest.kt`.

**Interfaces:**
- Consumes: Task 2 runtime interface and model state; current app language selection.
- Produces: local FAQ assistant in Kannada and English, with explicit non-medical scope and staff-contact fallback.

- [ ] **Step 1: Add focused failing tests**

Cover FAQ retrieval in both languages; no response before model readiness; prompt construction containing only the user question and approved public FAQ context; runtime exception; empty response; user leaving the screen during generation; and visible non-clinical disclaimer/staff fallback.

- [ ] **Step 2: Verify the relevant failure**

Run the focused unit tests. Expected: tests fail because repository/view-model conversation behavior is absent.

- [ ] **Step 3: Implement the minimum behavior**

Use only reviewed service FAQs such as booking navigation and general Care Solt app help. Do not add medical answers. Do not include signed-in identity, patient details, appointments, queue data, or medical records in prompts. Label responses as AI-assisted, show a clear non-medical disclaimer, and direct medical questions to qualified staff/clinicians without generating clinical guidance. Preserve the current English/Kannada app language toggle.

- [ ] **Step 4: Verify the focused pass**

Run FAQ and conversation tests. Expected: both languages return approved FAQ-grounded service information; unsafe/clinical scope is refused with a clinician/staff direction; runtime failures show a usable fallback.

- [ ] **Step 5: Run the affected integration check**

Run Android debug assemble and test on the target handset. Measure model load time, first-token/response latency, memory use, battery/thermal behavior, and Kannada/English answer quality. Record model/version/license and measured device details before deciding whether the feature can be enabled by default.

- [ ] **Step 6: Commit the passing deliverable**

```bash
git add android/app/src/main android/app/src/test
git commit -m "feat(android): add bilingual local service FAQ assistant"
```

## Task 4: Validate MCP compatibility and add public hospital discovery only if it passes

**Files:**
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/mcp/CareSoltMcpClient.kt` — narrow Streamable HTTP client for the existing MCP endpoint.
- Create (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/mcp/PublicHospitalSearchRepository.kt` — typed mapping of only public hospital fields.
- Modify (proposed): `android/app/src/main/java/in/caresolt/mobile/ai/AiAssistantScreen.kt` — explicit hospital-search action and result/error/empty states.
- Create (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/mcp/CareSoltMcpClientTest.kt`.
- Create (proposed): `android/app/src/test/java/in/caresolt/mobile/ai/mcp/PublicHospitalSearchRepositoryTest.kt`.

**Interfaces:**
- Consumes: existing public endpoint `https://www.caresolt.in/api/mcp`; only existing read-only tool `search_hospitals(query?, limit?)`.
- Produces: a typed list of returned public hospital directory records with name, address, phone and booking URL when supplied by the server; separate Empty and Error states.

- [ ] **Step 1: Add focused failing tests**

Use a mock HTTP server and recorded protocol fixtures to test MCP initialization/negotiation, tool listing, a valid search result, empty list, malformed response, HTTP failure, timeout, unsupported protocol response, and unexpected fields. Assert that only the public hospital fields are rendered and no private patient/appointment/queue information is requested or accepted as a feature.

- [ ] **Step 2: Verify the relevant failure**

Run focused MCP client tests. Expected: protocol tests fail until the client is implemented; record the endpoint's actual supported protocol behavior separately from Gallery-app success.

- [ ] **Step 3: Implement the minimum behavior**

First test the current endpoint's Streamable HTTP compatibility independently. Implement only the minimum protocol exchange needed to call `search_hospitals`; validate tool name and query/limit inputs against the published schema. Do not embed secrets. Do not add other tools. Do not silently treat timeout/server/protocol errors as an empty directory. If compatibility or security testing fails, do not enable this feature; keep local FAQs available and report the blocker.

- [ ] **Step 4: Verify the focused pass**

Run protocol and repository tests. Expected: valid returned records display; a true empty array displays a no-hospitals-found message; network/protocol/server failures display a retryable error; unapproved tools and private fields are not exposed.

- [ ] **Step 5: Run the affected integration check**

Run Android debug assemble and a live read-only smoke test against the public endpoint using a non-sensitive query. Expected: only actual active public directory results are shown; booking links direct users to the existing booking flow; no backend writes or production configuration changes occur.

- [ ] **Step 6: Commit the passing deliverable**

```bash
git add android/app/src/main android/app/src/test
git commit -m "feat(android): add read-only public hospital search"
```

## Unresolved product and environment decisions

1. **Target handset:** confirm exact device model, Android version, RAM and free storage before selecting a model. Do not infer these from an approximate device identifier.
2. **Model choice:** select only after checking license, download size, LiteRT-LM compatibility and on-device Kannada/English performance. If no model passes, ship the AI shell disabled rather than choosing an untested model.
3. **Hospital search UX:** decide whether it is a button/action in the assistant or model-initiated tool calling. Recommendation: start with an explicit user-tapped search action; model-initiated calls add complexity and should be a later separately reviewed change.
4. **MCP protocol support:** endpoint compatibility with Android Streamable HTTP has not yet been established. Gallery registration alone does not prove the native client's protocol behavior.
5. **Build runner:** the inspected README notes a Gradle wrapper may need to be generated. Confirm the branch's actual wrapper and CI setup before fixing exact local build commands; do not commit generated wrapper binaries without checking repository conventions.
6. **Release decision:** enabling the feature by default, publishing an APK, merging the PR, or deploying any service requires separate explicit approval.
