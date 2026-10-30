package com.caresolt.mobile.ui

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caresolt.mobile.data.BOOKING_RPC_VERIFIED
import com.caresolt.mobile.data.BookingForm

private val Teal = Color(0xFF087F8C)
private enum class Tab(val key: String) { HOME("home"), BOOKINGS("bookings"), QUEUE("queue"), HELP("help"), AI("ai") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareSoltPatientApp(vm: AppViewModel = viewModel()) {
    val s by vm.state.collectAsStateWithLifecycle()
    var tab by remember { mutableStateOf(Tab.HOME) }
    val tabs = Tab.values().filter { it != Tab.QUEUE || (s.signedIn && s.isStaff) }   // Queue: staff only
    if (tab !in tabs) tab = Tab.HOME
    val l = s.lang

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column { Text("Care Solt", style = MaterialTheme.typography.titleLarge); Text(t("tagline", l), style = MaterialTheme.typography.bodySmall) } },
                actions = {
                    TextButton(onClick = vm::toggleLang) { Icon(Icons.Default.Language, null); Spacer(Modifier.width(4.dp)); Text(if (l == "en") "ಕನ್ನಡ" else "EN") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tb ->
                    NavigationBarItem(
                        selected = tab == tb, onClick = { tab = tb },
                        icon = { Icon(when (tb) { Tab.HOME -> Icons.Default.Home; Tab.BOOKINGS -> Icons.Default.CalendarMonth; Tab.QUEUE -> Icons.Default.Groups; Tab.HELP -> Icons.Default.Chat; Tab.AI -> Icons.Default.AutoAwesome }, null) },
                        label = { Text(t(tb.key, l)) }
                    )
                }
            }
        }
    ) { pad ->
        Box(Modifier.padding(pad).fillMaxSize()) {
            when (tab) {
                Tab.HOME -> HomeScreen(s, vm) { tab = Tab.BOOKINGS }
                Tab.BOOKINGS -> if (s.signedIn) BookingsScreen(s, vm) else AuthScreen(s, vm)
                Tab.QUEUE -> InfoScreen(t("queue_title", l), t("queue_body", l))   // TODO: wire live queue data (staff RLS)
                Tab.HELP -> HelpScreen(l)
                Tab.AI -> AiScreen(l)
            }
        }
    }
}

@Composable private fun Banner(s: UiState) {
    val text = s.error ?: s.message?.let { t(it, s.lang) }
    if (text != null) Text(text, Modifier.fillMaxWidth().padding(bottom = 8.dp), color = if (s.error != null) MaterialTheme.colorScheme.error else Teal)
    if (s.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
}

// ---------------- Auth ----------------
@Composable
fun AuthScreen(s: UiState, vm: AppViewModel) {
    var email by remember { mutableStateOf("") }; var pw by remember { mutableStateOf("") }; var signup by remember { mutableStateOf(false) }
    val l = s.lang
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(t(if (signup) "sign_up" else "sign_in_title", l), style = MaterialTheme.typography.headlineMedium)
        Banner(s)
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text(t("email", l)) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        OutlinedTextField(pw, { pw = it }, Modifier.fillMaxWidth(), label = { Text(t("password", l)) }, singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Button({ if (signup) vm.signUp(email, pw) else vm.signIn(email, pw) }, Modifier.fillMaxWidth(), enabled = !s.busy && email.isNotBlank() && pw.length >= 6) {
            Text(t(if (signup) "sign_up" else "sign_in", l))
        }
        if (!signup) TextButton({ vm.recover(email) }, enabled = email.isNotBlank() && !s.busy) { Text(t("forgot", l)) }
        TextButton({ signup = !signup; vm.clearMessage() }) { Text(t(if (signup) "have_account" else "sign_up", l)) }
    }
}

// ---------------- Home: browse + booking flow (guest-safe: no patient data) ----------------
@Composable
fun HomeScreen(s: UiState, vm: AppViewModel, goBookings: () -> Unit) {
    val l = s.lang
    var query by remember { mutableStateOf("") }
    Column(Modifier.padding(horizontal = 16.dp)) {
        if (s.stage == Stage.HOSPITALS) {
            Card(colors = CardDefaults.cardColors(containerColor = Teal), shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(t("hero_title", l), style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text(t("hero_sub", l), color = Color.White)
                    Button({ /* scrolls user to the list below */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Teal)) {
                        Icon(Icons.Default.CalendarMonth, null); Spacer(Modifier.width(8.dp)); Text(t("book", l))
                    }
                }
            }
        } else TextButton(vm::back) { Icon(Icons.Default.ArrowBack, null); Text(t("back", l)) }
        Banner(s)
        when (s.stage) {
            Stage.HOSPITALS -> {
                OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(t("search_hospital", l)) }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
                val list = s.hospitals.filter { query.isBlank() || it.name.contains(query, true) || (it.address ?: "").contains(query, true) }
                PickList(list.map { Triple(it.name, it.address ?: "", it) }, s, vm::loadHospitals, vm::pickHospital)
            }
            Stage.DEPARTMENTS -> { Header(s.hospital?.name); PickList(s.departments.map { Triple(it.name, "", it) }, s, {}, vm::pickDepartment) }
            Stage.DOCTORS -> { Header(s.department?.name); PickList(s.doctors.map { Triple(it.display, it.specialization ?: "", it) }, s, {}, vm::pickDoctor) }
            Stage.SLOTS -> SlotPicker(s, vm, goBookings)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable private fun Header(text: String?) { if (text != null) Text(text, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp)) }

@Composable
private fun <T> PickList(items: List<Triple<String, String, T>>, s: UiState, retry: () -> Unit, onPick: (T) -> Unit) {
    if (items.isEmpty() && !s.busy) {
        Column(Modifier.padding(vertical = 16.dp)) { Text(t("empty", s.lang)); TextButton(retry) { Text(t("retry", s.lang)) } }
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 12.dp)) {
        items(items) { (title, sub, obj) ->
            Card(Modifier.fillMaxWidth().clickable { onPick(obj) }, shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(16.dp)) { Text(title, style = MaterialTheme.typography.titleMedium); if (sub.isNotBlank()) Text(sub, style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}

@Composable
private fun SlotPicker(s: UiState, vm: AppViewModel, goBookings: () -> Unit) {
    val l = s.lang
    var first by remember { mutableStateOf("") }; var last by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }; var email by remember { mutableStateOf("") }; var dob by remember { mutableStateOf("") }
    Header(s.doctor?.display)
    if (!s.signedIn) { Text(t("sign_in_title", l)); Button(goBookings) { Text(t("sign_in", l)) }; return }
    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(first, { first = it }, Modifier.fillMaxWidth(), label = { Text(t("first_name", l)) }, singleLine = true)
        OutlinedTextField(last, { last = it }, Modifier.fillMaxWidth(), label = { Text(t("last_name", l)) }, singleLine = true)
        OutlinedTextField(phone, { phone = it }, Modifier.fillMaxWidth(), label = { Text(t("phone", l)) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text(t("email", l)) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        OutlinedTextField(dob, { dob = it }, Modifier.fillMaxWidth(), label = { Text(t("dob", l)) }, singleLine = true)
        if (!BOOKING_RPC_VERIFIED) Text(t("booking_disabled", l), color = MaterialTheme.colorScheme.error)
        Text(t("slots", l), style = MaterialTheme.typography.titleMedium)
        s.slots.forEach { slot ->
            Card(Modifier.fillMaxWidth().clickable {
                if (BOOKING_RPC_VERIFIED && first.isNotBlank() && phone.length >= 10) vm.book(slot, BookingForm(first.trim(), last.trim(), phone.trim(), email.trim(), dob.trim()))
            }, shape = RoundedCornerShape(20.dp)) { Text(slot.label, Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium) }
        }
        // TODO: consultation-window schedules -> care.windows(scheduleId, date) + vm.bookWindow(...) once RPC signatures are final.
    }
}

// ---------------- My bookings ----------------
@Composable
fun BookingsScreen(s: UiState, vm: AppViewModel) {
    val l = s.lang
    LaunchedEffect(Unit) { vm.loadBookings() }
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(t("my_bookings", l), style = MaterialTheme.typography.headlineSmall)
            TextButton(vm::signOut) { Text(t("sign_out", l)) }
        }
        Banner(s)
        if (s.bookings.isEmpty() && !s.busy) Text(t("no_bookings", l))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(s.bookings) { b ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(listOfNotNull(b.date, b.time?.take(5)).joinToString("  "), style = MaterialTheme.typography.titleMedium)
                        Text(b.status ?: "")
                    }
                }
            }
        }
    }
}

// ---------------- Help / AI / Queue ----------------
@Composable
fun InfoScreen(title: String, body: String) = Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(title, style = MaterialTheme.typography.headlineMedium); Text(body)
}

@Composable
fun HelpScreen(l: String) {
    val faqs = listOf("faq1q" to "faq1a", "faq2q" to "faq2a", "faq3q" to "faq3a")
    LazyColumn(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)) {
        item { Text(t("help_title", l), style = MaterialTheme.typography.headlineMedium) }
        items(faqs) { (q, a) -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) { Column(Modifier.padding(16.dp)) { Text(t(q, l), style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(6.dp)); Text(t(a, l)) } } }
        item { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) { Column(Modifier.padding(16.dp)) { Text(t("privacy_title", l), style = MaterialTheme.typography.titleMedium); Text(t("privacy_body", l)) } } }
    }
}

@Composable
fun AiScreen(l: String) {
    val device = "${Build.MANUFACTURER} ${Build.MODEL}"   // real device, not hardcoded
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(t("ai_title", l), style = MaterialTheme.typography.headlineMedium)
        Text(t("ai_body", l)); Text("${t("ai_untested", l)} ($device)")
        // TODO: Gemma is license-gated — require accepted license + Hugging Face token before any download; benchmark CPU/GPU on-device first.
        Button({ /* TODO: open model terms URL */ }, Modifier.fillMaxWidth()) { Text(t("ai_terms", l)) }
        OutlinedButton({ }, Modifier.fillMaxWidth()) { Text(t("ai_skip", l)) }
        Text(t("ai_note", l), style = MaterialTheme.typography.bodySmall)
    }
}
