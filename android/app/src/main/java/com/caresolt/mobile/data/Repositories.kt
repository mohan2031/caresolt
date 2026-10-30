package com.caresolt.mobile.data

import kotlinx.serialization.json.*

/** Booking RPCs accept name/phone today. Keep OFF until RPC security + patient identity are verified server-side. */
// SECURITY: both booking RPCs are SECURITY DEFINER and can find-or-create a patient by phone number.
// Keep false until patient identity is bound to auth.uid() server-side (new migration, reviewed) — do NOT enable before.
const val BOOKING_RPC_VERIFIED = false

sealed interface SignUpResult { data object SignedIn : SignUpResult; data object ConfirmEmail : SignUpResult }

class AuthRepository(private val http: SupabaseHttp, private val store: SessionStore) {
    val isSignedIn get() = store.accessToken != null

    suspend fun signIn(email: String, password: String) {
        val res = http.request("POST", "/auth/v1/token", mapOf("grant_type" to "password"),
            buildJsonObject { put("email", email.trim()); put("password", password) }.toString(), anon = true)
        save(http.json.decodeFromString<AuthSession>(res))
    }

    suspend fun signUp(email: String, password: String): SignUpResult {
        val res = http.request("POST", "/auth/v1/signup", body =
            buildJsonObject { put("email", email.trim()); put("password", password) }.toString(), anon = true)
        val o = http.json.parseToJsonElement(res).jsonObject
        return if (o["access_token"] != null) { save(http.json.decodeFromString<AuthSession>(res)); SignUpResult.SignedIn }
        else SignUpResult.ConfirmEmail
    }

    suspend fun recover(email: String) {
        http.request("POST", "/auth/v1/recover", body = buildJsonObject { put("email", email.trim()) }.toString(), anon = true)
    }

    fun signOut() = store.clearSession()

    data class Identity(val role: String?, val patientId: String?)

    /** users.role and patients.id via patients.user_id (verified: both match the auth user id). Missing rows -> nulls, handled safely. */
    suspend fun identity(): Identity {
        val uid = store.userId ?: return Identity(null, null)
        val role = runCatching { http.get<List<UserRow>>("/rest/v1/users", mapOf("id" to "eq.$uid", "select" to "id,role", "limit" to "1")).firstOrNull()?.role }.getOrNull()
        val pid = runCatching { http.get<List<Patient>>("/rest/v1/patients", mapOf("user_id" to "eq.$uid", "select" to "id,user_id", "limit" to "1")).firstOrNull()?.id }.getOrNull()
        return Identity(role, pid)
    }

    private fun save(s: AuthSession) {
        store.accessToken = s.accessToken; store.refreshToken = s.refreshToken
        store.userId = s.user.id; store.email = s.user.email
    }
}

class CareRepository(private val http: SupabaseHttp) {
    suspend fun hospitals(): List<Hospital> =
        http.get("/rest/v1/hospitals", mapOf("active" to "eq.true", "select" to "*", "order" to "name.asc"))

    suspend fun departments(hospitalId: String): List<Department> =
        http.get("/rest/v1/departments", mapOf("hospital_id" to "eq.$hospitalId", "active" to "eq.true", "select" to "*", "order" to "name.asc"))

    /** doctor_schedules -> distinct doctor ids -> get_public_doctors_for_booking(p_doctor_ids). TODO(verify): schedule filter columns. */
    suspend fun doctors(hospitalId: String, departmentId: String): List<Doctor> {
        val sch = http.get<List<DoctorSchedule>>("/rest/v1/doctor_schedules",
            mapOf("hospital_id" to "eq.$hospitalId", "department_id" to "eq.$departmentId", "active" to "eq.true", "select" to "id,doctor_id"))
        val ids = sch.map { it.doctorId }.distinct()
        if (ids.isEmpty()) return emptyList()
        return http.rpc("get_public_doctors_for_booking", buildJsonObject { put("p_doctor_ids", JsonArray(ids.map { JsonPrimitive(it) })) })
    }

    private fun today(): String = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())

    private suspend fun scheduleIds(doctorId: String): List<String> =
        http.get<List<DoctorSchedule>>("/rest/v1/doctor_schedules", mapOf("doctor_id" to "eq.$doctorId", "active" to "eq.true", "select" to "id,doctor_id")).map { it.id }

    /** Fixed slots: available and today-or-later only. */
    suspend fun slots(doctorId: String): List<Slot> {
        val ids = scheduleIds(doctorId); if (ids.isEmpty()) return emptyList()
        return http.get("/rest/v1/schedule_slots", mapOf("schedule_id" to "in.(${ids.joinToString(",")})", "is_available" to "eq.true", "slot_date" to "gte.${today()}", "select" to "*", "order" to "slot_date.asc,start_time.asc"))
    }

    /** Verified RPC signature: (p_doctor_id, p_date). TODO: wire into UI for window-based schedules. */
    suspend fun windows(doctorId: String, date: String): List<ConsultationWindow> =
        http.rpc("get_public_consultation_windows", buildJsonObject { put("p_doctor_id", doctorId); put("p_date", date) })

    private fun JsonObjectBuilder.patient(f: BookingForm) {
        put("p_patient_first_name", f.firstName); put("p_patient_last_name", f.lastName)
        put("p_patient_phone", f.phone); put("p_patient_email", f.email); put("p_patient_dob", f.dob)
    }

    /** Verified params. Gated until RPC auth/identity behavior is security-reviewed. */
    suspend fun bookSlot(slotId: String, f: BookingForm) {
        check(BOOKING_RPC_VERIFIED) { "Booking RPC not verified yet" }
        http.request("POST", "/rest/v1/rpc/book_appointment", body = buildJsonObject { put("p_slot_id", slotId); patient(f) }.toString())
    }

    /** TODO(verify): extra params (e.g. window start) beyond p_schedule_id, p_date + patient fields. */
    suspend fun bookWindow(scheduleId: String, date: String, f: BookingForm) {
        check(BOOKING_RPC_VERIFIED) { "Booking RPC not verified yet" }
        http.request("POST", "/rest/v1/rpc/book_consultation_window", body = buildJsonObject { put("p_schedule_id", scheduleId); put("p_date", date); patient(f) }.toString())
    }

    /** TODO(verify): appointments.patient_id references patients.id; RLS must restrict to own rows. */
    suspend fun myBookings(patientId: String): List<Appointment> =
        http.get("/rest/v1/appointments", mapOf("patient_id" to "eq.$patientId", "select" to "*", "order" to "scheduled_date.desc"))
}
