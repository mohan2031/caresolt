# CareSolt — Multi-Hospital Master Requirements

## Product model

CareSolt is a **multi-tenant healthcare platform** for many independent healthcare organizations, hospitals and branches. It is not a single-hospital application.

**Online booking is optional. Hospital check-in is mandatory.**

## Organization hierarchy

CareSolt Platform → Healthcare Organization → Hospital/Branch → Department → Doctor → Schedule → Slots → Appointment/Visit → Check-in → Queue → Consultation.

Each tenant's operational data must be isolated. A receptionist, doctor or administrator must only access organizations/locations granted to their role. Platform-level super-admin access is separate from hospital-level administration.

## Online booking

Patient books from home for a future date/time:

1. Select hospital/branch and department.
2. Select doctor.
3. Select a real available time slot.
4. Confirm appointment.
5. The appointment reserves that slot immediately and concurrent requests cannot double-book it.
6. Patient receives confirmation.
7. Patient does not need to book again at the hospital.

## Hospital arrival — online booking

Patient arrives → reception → appointment retrieval → identity/booking verification → check-in → queue activation → real-time Patient Ready update to the assigned doctor.

## Walk-in

Patient arrives without booking → reception searches or creates patient record → selects appropriate hospital/branch, department and doctor/flow → creates visit/appointment entry → check-in → queue/token → Patient Ready update.

## Queue principles

Real-time queue state; predictable policy-based ordering; controlled actions for no-show, skip, delay, reassignment and urgent cases; idempotent operations; auditability. Do not promise zero waiting. Measure and reduce avoidable waiting.

## Doctor workflow

Doctor sees authorized schedule and live queue. Patient Ready appears without manual phone/radio coordination. Doctor opens authorized Patient 360 context, documents consultation, creates prescription and downstream orders when enabled.

## Patient 360

Maintain a longitudinal patient identity while enforcing organization/location and role-based access boundaries. Connect demographics, visits, appointments, relevant clinical records, prescriptions, allergies, documents, referrals and follow-up as the applicable modules are implemented.

## Roles

Platform Super Admin; Organization Admin; Hospital/Branch Admin; Reception/Front Desk; Doctor; Nurse/Staff; Lab; Pharmacy; Billing/Cashier; Patient. Permissions are server-enforced and least-privilege.

## Production architecture requirements

- Multi-tenant relational data model with tenant/organization and location scope on operational entities.
- PostgreSQL-style transactional persistence.
- Database/transaction safeguards against double booking.
- Real-time queue events.
- Strong authentication and server-side authorization.
- Audit logs for booking, check-in, queue, consultation and permission-sensitive actions.
- No cross-tenant data leakage.
- Mobile-responsive patient and staff experiences.
- Real loading, empty, error and success states.
- Automated tests for tenant isolation, authorization, concurrent booking, check-in and queue transitions.
- Interoperability layer prepared for ABDM/FHIR without forcing raw FHIR to be the operational database.

## P0 — Smart OPD Core

Tenant/org/hospital setup, authentication/RBAC, patient registration/profile, hospital/branch/department structure, doctor schedules, real-time slots, online booking, walk-in flow, reception check-in, token/queue, doctor live queue, Patient 360 foundation, consultation, prescription foundation, notifications, audit logging and administration.

## P1 — Connected Care

Lab, pharmacy, billing, documents, follow-up, payments, messaging and telemedicine.

## P2 — Interoperability

ABHA/ABDM, FHIR exchange, external lab/healthcare interfaces and consent workflows.

## P3 — Intelligence and scale

AI documentation assistance, analytics, multi-location enterprise controls and partner APIs.

## Core KPI direction

Booking completion, check-in duration, walk-in registration duration, check-in-to-ready time, waiting time, doctor utilization, no-show rate, queue accuracy, double-booking incidents (target zero), repeated-data-entry events and completed patient journeys.

## Definition of done

A workflow is complete only when UI, backend, database, authorization, validation, error handling, tests and deployment verification work end-to-end. Never claim a feature is implemented when it is only mocked.
