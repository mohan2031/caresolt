# CareSolt — Master Requirements

## Core product rule

**Online booking is optional. Hospital check-in is mandatory.**

CareSolt is a hospital patient-flow platform focused on reducing avoidable waiting and repeated manual work.

## Online booking

Patient books from home for a future date/time:

1. Select hospital/department.
2. Select doctor.
3. Select a real available time slot.
4. Confirm appointment.
5. The appointment reserves that slot immediately so another patient cannot double-book it.
6. Patient receives confirmation and appointment details.
7. Patient does not need to book again at the hospital.

## Hospital arrival — online booking

1. Patient arrives.
2. Patient goes to reception.
3. Reception retrieves the confirmed appointment.
4. Reception performs check-in.
5. CareSolt marks the appointment as arrived/checked-in.
6. CareSolt generates or activates the queue/token according to the hospital's queue policy.
7. Doctor dashboard receives a real-time **Patient Ready** update.
8. Doctor calls/advances the patient from the CareSolt queue.

## Walk-in

1. Patient arrives without online booking.
2. Reception searches an existing patient or creates a patient record.
3. Reception selects the appropriate doctor/department and available walk-in flow.
4. CareSolt creates the same-day appointment/visit entry.
5. Reception checks the patient in.
6. CareSolt generates the queue/token.
7. Doctor receives the Patient Ready update.

## Queue principles

- Real-time queue state.
- Default FIFO ordering within the applicable appointment/queue policy.
- Controlled staff actions for no-show, skip, delay, reassignment and urgent cases.
- Queue operations must be idempotent.
- Patients should not repeatedly provide the same information.
- Do not promise zero waiting; optimize and measure avoidable waiting.

## Doctor workflow

Doctor sees their schedule and live queue. For a ready patient, CareSolt presents an authorized Patient 360 view. Doctor can document the encounter, create prescriptions and create downstream orders where those modules are enabled.

## Future-connected workflow

Consultation may hand off to Lab, Pharmacy, Billing, referral and follow-up workflows. These are designed as modular extensions and must remain connected to the same patient and encounter identity.

## Roles

Patient, Reception/Front Desk, Doctor, Nurse/Staff, Lab, Pharmacy, Billing/Cashier, Administrator and Super Admin, with strict server-side authorization and least privilege.

## Production requirements

- Relational database, preferably PostgreSQL.
- Transactional protection against double booking.
- Real-time queue updates using an appropriate push mechanism.
- Audit logs for important operations.
- Secure authentication and RBAC.
- Sensitive health information protected by design.
- Mobile-responsive UX.
- Loading, empty, error and success states.
- Automated tests for booking concurrency, check-in, queue transitions and authorization.
- ABDM/FHIR-ready interoperability layer without forcing raw FHIR to be the operational database.

## Primary KPI direction

Measure and improve:

- booking completion rate
- check-in time
- registration time for walk-ins
- time from check-in to ready/queue
- waiting time
- doctor utilization
- no-show rate
- queue accuracy
- double-booking incidents (target: zero)
- repeated-data-entry events
- patient journey completion

## Release strategy

### P0 — Smart OPD Core
Authentication, roles, patient registration/profile, doctor schedules, online booking, walk-in flow, reception check-in, queue/token management, doctor queue, Patient 360 foundation, consultation, prescription foundation, notifications, audit logging and admin controls.

### P1 — Connected Care
Lab, pharmacy, billing, documents, follow-up, payments, messaging and telemedicine as required.

### P2 — Interoperability
ABDM/ABHA/FHIR integration and external healthcare interfaces.

### P3 — Intelligence and scale
AI documentation assistance, analytics, multi-location/enterprise capabilities and partner APIs.

## Definition of done

A workflow is not complete because its UI exists. It must work end-to-end across UI, backend, database, authorization, validation, error handling, tests and deployment verification.
