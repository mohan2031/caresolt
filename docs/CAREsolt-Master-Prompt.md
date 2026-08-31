# CareSolt — Master Build Prompt

Build CareSolt as a production-oriented hospital patient-flow platform, not a throwaway demo.

## Non-negotiable product model

**Online booking is optional. Hospital check-in is mandatory.**

Support two entry modes:

### Online booking
Patient books from home for a future date/time by selecting doctor and a real available slot. Confirmation reserves the slot immediately. Prevent double booking transactionally. At hospital arrival the patient goes to reception; reception retrieves the booking and checks the patient in. The patient must not book again. Check-in activates the queue and triggers a real-time Patient Ready event to the assigned doctor's dashboard.

### Walk-in
Patient comes directly to the hospital without a booking. Reception searches or creates the patient record, selects the appropriate doctor/department and walk-in flow, creates the same-day visit/appointment entry, checks the patient in, and CareSolt adds the patient to the queue and notifies the doctor.

## Product objective

Reduce avoidable waiting, repeated registration and manual coordination. Do not claim 100% zero waiting; measure and reduce avoidable delay.

## Core workflow

Book or Walk In → Reception Check-in → Queue/Token → Patient Ready → Doctor Consultation → Next Step.

## Doctor experience

The doctor dashboard must show schedule, live queue and Patient Ready cards. The doctor should not depend on phone/radio calls from reception to know who is ready. Opening a patient shows an authorized Patient 360 view. Consultation can produce notes, prescriptions and downstream orders where enabled.

## Queue

Use real-time state. Default ordering should be predictable and fair under the hospital's policy, with controlled staff actions for no-show, skip, delay, reassignment and urgent cases. Operations must be idempotent and auditable.

## Architecture

Use a maintainable modular architecture and a relational transactional data model. Protect appointment allocation against concurrent booking attempts. Use server-side authorization and least privilege. Use real-time push updates for queue state. Never put secrets in client code.

Design an interoperability layer so the internal operational model can later map to ABDM/FHIR without making raw FHIR the operational database.

## Initial P0

- Authentication
- Patient registration/profile
- Doctor/provider profiles and schedules
- Real-time appointment slots
- Online appointment booking and confirmation
- Walk-in registration
- Reception check-in
- Token/queue management
- Doctor live queue
- Patient Ready notification
- Patient 360 foundation
- Consultation and prescription foundation
- Notifications
- Audit logging
- Admin controls

## Future modules

Keep Lab, Pharmacy, Billing, Documents, Payments, Telemedicine, ABDM/FHIR, AI documentation and advanced analytics modular. Do not pretend future modules are implemented.

## Quality

Every major workflow needs loading, empty, error and success states; responsive mobile/tablet/desktop behavior; validation; authorization; auditability; automated tests; and production deployment verification.

Do not claim a feature, test, or deployment is complete unless it has actually been verified.
