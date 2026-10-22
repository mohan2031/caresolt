# Care Solt Android (starter)

A separate native Android application foundation for the existing Care Solt platform. Google AI Edge Gallery is a reference for future optional on-device AI; this project does not embed or copy the Gallery app.

## Current state
- Kotlin + Jetpack Compose app shell.
- Home, appointments, queue and bilingual help screens.
- English/Kannada UI toggle.
- No mock patient, appointment, doctor, hospital or queue records.
- No backend credentials or service-role keys are included.
- HTTPS-only network policy; cleartext HTTP is disabled.
- Appointment and queue screens explicitly state that secure API integration is required.

## Open in Android Studio
Open the `android/` directory as a project. Android Studio should sync the Gradle Kotlin DSL files. If your environment requires a Gradle wrapper, generate it with the Gradle version supported by the installed Android Gradle Plugin.

Requirements: JDK 17, Android SDK 35, Android Studio with Kotlin/Compose support.

## Before connecting production
1. Inspect the current Care Solt auth flow and API contracts.
2. Implement the existing supported sign-in flow; never collect passwords in a custom screen or bypass OAuth/session checks.
3. Use the existing backend endpoints and preserve server-side role checks, tenant isolation and Supabase RLS.
4. Keep secrets out of the app. A mobile client is a public client.
5. Add API contract tests and tests for patient/doctor/hospital isolation.
6. Add local AI only as a separate optional module, after evaluating a supported Google AI Edge model on target devices.
7. Never send patient-identifiable data to a model unless explicitly required, authorized, and covered by the approved privacy design.

## Not production-ready yet
This is a UI and project scaffold, not a released app. Backend authentication, real API integration, automated tests, model integration, app signing and release QA remain to be completed. No production website or database was modified.
