package com.caresolt.mobile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AuthUser(val id: String, val email: String? = null)
@Serializable data class AuthSession(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    val user: AuthUser
)

/** Verified: role is in users.role; patient identity is patients.user_id. */
@Serializable data class UserRow(val id: String, val role: String? = null)
@Serializable data class Patient(val id: String, @SerialName("user_id") val userId: String? = null)
@Serializable data class DoctorSchedule(val id: String, @SerialName("doctor_id") val doctorId: String)

/** Patient details required by the booking RPCs (p_patient_first_name, _last_name, _phone, _email, _dob). */
data class BookingForm(val firstName: String, val lastName: String, val phone: String, val email: String, val dob: String)

/** Verified: get_public_consultation_windows(p_doctor_id, p_date) returns these columns. */
@Serializable data class ConsultationWindow(
    @SerialName("schedule_id") val scheduleId: String,
    @SerialName("start_time") val start: String? = null,
    @SerialName("end_time") val end: String? = null,
    val capacity: Int? = null,
    @SerialName("booked_count") val bookedCount: Int? = null,
    @SerialName("remaining_count") val remaining: Int? = null,
    @SerialName("window_code") val windowCode: String? = null
) { val label get() = listOfNotNull(start?.take(5), end?.take(5)).joinToString(" – ") + (remaining?.let { "  ($it left)" } ?: "") }

/** Verified columns: hospitals(id, name, address, active). */
@Serializable data class Hospital(val id: String, val name: String, val address: String? = null)
/** Verified columns: departments(id, hospital_id, name, description, active). */
@Serializable data class Department(
    val id: String, @SerialName("hospital_id") val hospitalId: String? = null,
    val name: String, val description: String? = null
)

/** Verified: RPC takes p_doctor_ids, returns id, full_name, specialization, qualification. */
@Serializable data class Doctor(
    val id: String, val name: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    val specialization: String? = null,
    val qualification: String? = null
) { val display get() = fullName ?: name ?: "Doctor" }

/** Verified: schedule_id, slot_date, start_time, end_time, is_available. */
@Serializable data class Slot(
    val id: String,
    @SerialName("slot_date") val date: String? = null,
    @SerialName("start_time") val start: String? = null,
    @SerialName("end_time") val end: String? = null,
    @SerialName("doctor_id") val doctorId: String? = null
) { val label get() = listOfNotNull(date, start?.take(5)).joinToString("  ") }

/** Verified: scheduled_date / scheduled_time. TODO(verify): patient_id column. */
@Serializable data class Appointment(
    val id: String,
    val status: String? = null,
    @SerialName("scheduled_date") val date: String? = null,
    @SerialName("scheduled_time") val time: String? = null,
    @SerialName("hospital_id") val hospitalId: String? = null
)

/** Verified users.role enum minus 'patient'. UI gating only; backend RLS is the real enforcement. */
val STAFF_ROLES = setOf("platform_super_admin", "org_admin", "hospital_admin", "reception", "doctor", "nurse", "lab", "pharmacy", "billing")
