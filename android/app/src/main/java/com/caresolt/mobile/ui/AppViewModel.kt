package com.caresolt.mobile.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.caresolt.mobile.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Stage { HOSPITALS, DEPARTMENTS, DOCTORS, SLOTS }

data class UiState(
    val lang: String = "en",
    val signedIn: Boolean = false,
    val email: String? = null,
    val role: String? = null,
    val busy: Boolean = false,
    val message: String? = null,       // transient info (reset sent, booked, ...)
    val error: String? = null,
    val hospitals: List<Hospital> = emptyList(),
    val departments: List<Department> = emptyList(),
    val doctors: List<Doctor> = emptyList(),
    val slots: List<Slot> = emptyList(),
    val stage: Stage = Stage.HOSPITALS,
    val hospital: Hospital? = null,
    val department: Department? = null,
    val doctor: Doctor? = null,
    val bookings: List<Appointment> = emptyList()
) { val isStaff get() = role?.lowercase()?.let { it in STAFF_ROLES } == true }

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val store = SessionStore(app)
    private val http = SupabaseHttp(store)
    private val auth = AuthRepository(http, store)
    private val care = CareRepository(http)
    private var patientId: String? = null

    private val _s = MutableStateFlow(UiState(lang = store.lang, signedIn = auth.isSignedIn, email = store.email))
    val state: StateFlow<UiState> = _s

    init { loadHospitals(); if (auth.isSignedIn) loadProfile() }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        _s.update { it.copy(busy = true, error = null) }
        try { block() } catch (e: ApiException) {
            if (e.code == 401) { auth.signOut(); _s.update { it.copy(signedIn = false, role = null) } }
            _s.update { it.copy(error = e.message) }
        } catch (e: Exception) { _s.update { it.copy(error = e.message ?: "Network error") } }
        finally { _s.update { it.copy(busy = false) } }
    }

    fun toggleLang() { val n = if (_s.value.lang == "en") "kn" else "en"; store.lang = n; _s.update { it.copy(lang = n) } }
    fun clearMessage() = _s.update { it.copy(message = null, error = null) }

    // ---- Auth ----
    fun signIn(email: String, pw: String) = launch { auth.signIn(email, pw); _s.update { it.copy(signedIn = true, email = store.email) }; loadProfileNow() }
    fun signUp(email: String, pw: String) = launch {
        when (auth.signUp(email, pw)) {
            SignUpResult.SignedIn -> { _s.update { it.copy(signedIn = true, email = store.email) }; loadProfileNow() }
            SignUpResult.ConfirmEmail -> _s.update { it.copy(message = "confirm_email") }
        }
    }
    fun recover(email: String) = launch { auth.recover(email); _s.update { it.copy(message = "reset_sent") } }
    fun signOut() { auth.signOut(); patientId = null; _s.update { it.copy(signedIn = false, role = null, bookings = emptyList(), email = null) } }

    private fun loadProfile() = launch { loadProfileNow() }
    private suspend fun loadProfileNow() {
        val id = auth.identity(); patientId = id.patientId
        _s.update { it.copy(role = id.role) }   // Queue tab visibility comes from here; server RLS still enforces access
    }

    // ---- Browse / booking ----
    fun loadHospitals() = launch { val h = care.hospitals(); _s.update { it.copy(hospitals = h) } }
    fun pickHospital(h: Hospital) = launch { val d = care.departments(h.id); _s.update { it.copy(hospital = h, departments = d, stage = Stage.DEPARTMENTS) } }
    fun pickDepartment(d: Department) = launch {
        val doc = care.doctors(_s.value.hospital!!.id, d.id); _s.update { it.copy(department = d, doctors = doc, stage = Stage.DOCTORS) }
    }
    fun pickDoctor(d: Doctor) = launch { val sl = care.slots(d.id); _s.update { it.copy(doctor = d, slots = sl, stage = Stage.SLOTS) } }
    fun back() = _s.update {
        it.copy(error = null, stage = when (it.stage) { Stage.SLOTS -> Stage.DOCTORS; Stage.DOCTORS -> Stage.DEPARTMENTS; else -> Stage.HOSPITALS })
    }
    fun book(slot: Slot, form: BookingForm) = launch {
        care.bookSlot(slot.id, form); _s.update { it.copy(message = "booked", stage = Stage.HOSPITALS) }; loadBookingsNow()
    }

    fun loadBookings() = launch { loadBookingsNow() }
    private suspend fun loadBookingsNow() {
        if (patientId == null) loadProfileNow()
        val id = patientId ?: return
        val b = care.myBookings(id); _s.update { it.copy(bookings = b) }
    }
}
