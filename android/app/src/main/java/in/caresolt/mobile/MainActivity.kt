package `in`.caresolt.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Teal = Color(0xFF087F8C)
private val Ink = Color(0xFF15323A)
private val Pale = Color(0xFFF3F8F8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CareSoltApp() }
    }
}

private enum class AppTab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Default.HealthAndSafety),
    Appointments("Bookings", Icons.Default.CalendarMonth),
    Queue("Queue", Icons.Default.Queue),
    Help("Help", Icons.Default.Chat)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CareSoltApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var kannada by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Teal,
            onPrimary = Color.White,
            background = Pale,
            surface = Color.White,
            onSurface = Ink
        )
    ) {
        Scaffold(
            containerColor = Pale,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Teal, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.HealthAndSafety,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Column(Modifier.padding(start = 10.dp)) {
                                Text("Care Solt", fontWeight = FontWeight.Bold)
                                Text(
                                    if (kannada) "ಆರೋಗ್ಯ ಸೇವೆಗಳು" else "Healthcare, made simpler",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    },
                    actions = {
                        androidx.compose.material3.IconButton(onClick = { kannada = !kannada }) {
                            Icon(Icons.Default.Language, contentDescription = "Change language")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Pale)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    AppTab.entries.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (AppTab.entries[selectedTab]) {
                AppTab.Home -> HomeScreen(innerPadding, kannada, onBook = { selectedTab = 1 })
                AppTab.Appointments -> InfoScreen(
                    innerPadding,
                    title = if (kannada) "ನಿಮ್ಮ ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್‌ಗಳು" else "Your appointments",
                    body = if (kannada) "ನಿಮ್ಮ ನಿಜವಾದ ಬುಕಿಂಗ್‌ಗಳನ್ನು ಸುರಕ್ಷಿತವಾಗಿ ತೋರಿಸಲು Care Solt ಬ್ಯಾಕೆಂಡ್ ಸಂಪರ್ಕ ಅಗತ್ಯ." else "Connect the existing Care Solt authenticated API to display real bookings. No demo appointments are shown."
                )
                AppTab.Queue -> InfoScreen(
                    innerPadding,
                    title = if (kannada) "ಲೈವ್ ಕ್ಯೂ" else "Live queue",
                    body = if (kannada) "ಆಸ್ಪತ್ರೆಯ ಅನುಮತಿಯೊಂದಿಗೆ ಮಾತ್ರ ನೈಜ ಕ್ಯೂ ಸ್ಥಿತಿಯನ್ನು ಪಡೆಯಲಾಗುತ್ತದೆ." else "Live queue data will come only from the existing authorized backend. Patient and staff permissions remain enforced."
                )
                AppTab.Help -> HelpScreen(innerPadding, kannada)
            }
        }
    }
}

@Composable
private fun HomeScreen(padding: PaddingValues, kannada: Boolean, onBook: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Teal)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text(
                        if (kannada) "ನಿಮ್ಮ ಆರೋಗ್ಯಕ್ಕೆ ಸರಳ ದಾರಿ" else "Your health, made simpler",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (kannada) "ಆಸ್ಪತ್ರೆಯ ಸೇವೆಗಳು ಮತ್ತು ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್‌ಗಳನ್ನು ಒಂದೇ ಸ್ಥಳದಲ್ಲಿ." else "Find care, manage appointments and get help in your preferred language.",
                        color = Color.White.copy(alpha = 0.92f)
                    )
                    Spacer(Modifier.height(18.dp))
                    Button(onClick = onBook) {
                        Text(if (kannada) "ಬುಕಿಂಗ್ ಬಗ್ಗೆ" else "Appointments")
                    }
                }
            }
        }
        item {
            Text(
                if (kannada) "ನಿಮಗೆ ಬೇಕಾದ ಸೇವೆ" else "Quick access",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            FeatureCard(
                Icons.Default.CalendarMonth,
                if (kannada) "ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್" else "Book an appointment",
                if (kannada) "ನಿಮ್ಮ ಆಸ್ಪತ್ರೆಯ ನೈಜ ಬುಕಿಂಗ್ ವ್ಯವಸ್ಥೆಗೆ ಸಂಪರ್ಕಿಸಿದ ನಂತರ ಲಭ್ಯ." else "Secure booking will use the existing Care Solt backend.",
                onClick = onBook
            )
        }
        item {
            FeatureCard(
                Icons.Default.Queue,
                if (kannada) "ಲೈವ್ ಕ್ಯೂ" else "Live patient queue",
                if (kannada) "ಅನುಮತಿಯಿರುವ ಬಳಕೆದಾರರಿಗೆ ಮಾತ್ರ." else "Visible only after secure sign-in and authorization.",
                onClick = {}
            )
        }
        item {
            FeatureCard(
                Icons.Default.SupportAgent,
                if (kannada) "ಸಹಾಯ" else "Kannada & English help",
                if (kannada) "ಸಾಮಾನ್ಯ, ವೈದ್ಯಕೀಯವಲ್ಲದ ಪ್ರಶ್ನೆಗಳಿಗೆ ಮಾಹಿತಿ." else "Start with reviewed, non-clinical FAQs; local AI can be added after evaluation.",
                onClick = {}
            )
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Teal)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(
                            if (kannada) "ನಿಮ್ಮ ಮಾಹಿತಿಯ ಸುರಕ್ಷತೆ ಮುಖ್ಯ" else "Your privacy comes first",
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (kannada) "ಈ ಆರಂಭಿಕ ಆವೃತ್ತಿ ನೈಜ ರೋಗಿ ದಾಖಲೆಗಳನ್ನು ತೋರಿಸುವುದಿಲ್ಲ. ಸುರಕ್ಷಿತ ಲಾಗಿನ್ ಮತ್ತು API ಸಂಪರ್ಕ ಇನ್ನೂ ಸಂಯೋಜಿಸಬೇಕಿದೆ." else "This starter does not display real patient data. Authentication and API integration must be completed before real-world use.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeatureCard(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(48.dp).background(Color(0xFFE3F3F3), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Teal)
            }
            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun InfoScreen(padding: PaddingValues, title: String, body: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(body, style = MaterialTheme.typography.bodyLarge)
        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFFF2D6)) {
            Text(
                "Integration required — this screen intentionally does not use sample patient data.",
                modifier = Modifier.padding(14.dp),
                color = Color(0xFF694D0A),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HelpScreen(padding: PaddingValues, kannada: Boolean) {
    val faqs = if (kannada) listOf(
        "ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್ ಹೇಗೆ ಬುಕ್ ಮಾಡುವುದು?" to "Care Solt ನಲ್ಲಿ ನಿಮ್ಮ ಆಸ್ಪತ್ರೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ ಮತ್ತು ಲಭ್ಯವಿರುವ ಬುಕಿಂಗ್ ಹಂತಗಳನ್ನು ಅನುಸರಿಸಿ. ನೈಜ ಬುಕಿಂಗ್‌ಗಾಗಿ ಸುರಕ್ಷಿತ ಸಂಪರ್ಕ ಅಗತ್ಯ.",
        "ನನ್ನ ಬುಕಿಂಗ್ ಸ್ಥಿತಿ ಎಲ್ಲಿದೆ?" to "ನಿಮ್ಮ ಬುಕಿಂಗ್ ಸ್ಥಿತಿಯನ್ನು ತೋರಿಸಲು ನಿಮ್ಮ ಖಾತೆಗೆ ಸುರಕ್ಷಿತವಾಗಿ ಲಾಗಿನ್ ಆಗಿರಬೇಕು.",
        "ವೈದ್ಯಕೀಯ ಸಲಹೆ ಬೇಕೇ?" to "ಈ ಸಹಾಯ ವಿಭಾಗ ವೈದ್ಯಕೀಯ ಸಲಹೆ ನೀಡುವುದಿಲ್ಲ. ನಿಮ್ಮ ವೈದ್ಯರು ಅಥವಾ ಸ್ಥಳೀಯ ತುರ್ತು ಸೇವೆಯನ್ನು ಸಂಪರ್ಕಿಸಿ."
    ) else listOf(
        "How do I book an appointment?" to "Choose your hospital and follow the available booking steps. Real bookings require the secure Care Solt backend connection.",
        "Where can I see my booking status?" to "Sign in securely to view your own booking status once account integration is enabled.",
        "Can this assistant give medical advice?" to "No. This help section provides general service information only. Contact a qualified clinician for medical advice or local emergency services for urgent help."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                if (kannada) "Care Solt ಸಹಾಯ" else "Care Solt Help",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (kannada) "ಪರಿಶೀಲಿಸಿದ ಸಾಮಾನ್ಯ ಮಾಹಿತಿಯಷ್ಟೇ" else "Reviewed service information only",
                color = Color.Gray
            )
        }
        items(faqs.size) { index ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text(faqs[index].first, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(faqs[index].second, color = Color.DarkGray)
                }
            }
        }
        item {
            Text(
                if (kannada) "ಸ್ಥಳೀಯ AI ಮಾದರಿ ಭವಿಷ್ಯದಲ್ಲಿ ಸೇರಿಸಬಹುದು; ಅದರ ಉತ್ತರಗಳನ್ನು ಪರಿಶೀಲಿಸಬೇಕು." else "A local on-device model may be added after device, privacy and language-quality evaluation. Model answers must not be treated as medical advice.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
