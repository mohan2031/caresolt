package com.caresolt.mobile.ui

private val S: Map<String, Pair<String, String>> = mapOf(
    "tagline" to ("Healthcare, made simple" to "ಆರೋಗ್ಯ ಸೇವೆ, ಸರಳವಾಗಿ"),
    "home" to ("Home" to "ಮುಖಪುಟ"), "bookings" to ("Bookings" to "ಬುಕಿಂಗ್‌ಗಳು"),
    "queue" to ("Queue" to "ಸರತಿ"), "help" to ("Help" to "ಸಹಾಯ"), "ai" to ("AI" to "AI"),
    "hero_title" to ("Your health, made simpler" to "ನಿಮ್ಮ ಆರೋಗ್ಯ, ಸರಳವಾಗಿ"),
    "hero_sub" to ("Find care, manage appointments and get help in your preferred language." to "ಆರೈಕೆ ಹುಡುಕಿ, ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್ ನಿರ್ವಹಿಸಿ ಮತ್ತು ನಿಮ್ಮ ಭಾಷೆಯಲ್ಲಿ ಸಹಾಯ ಪಡೆಯಿರಿ."),
    "book" to ("Book an appointment" to "ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್ ಬುಕ್ ಮಾಡಿ"),
    "search_hospital" to ("Search hospitals" to "ಆಸ್ಪತ್ರೆ ಹುಡುಕಿ"),
    "departments" to ("Departments" to "ವಿಭಾಗಗಳು"), "doctors" to ("Doctors" to "ವೈದ್ಯರು"),
    "slots" to ("Available times" to "ಲಭ್ಯ ಸಮಯಗಳು"), "confirm" to ("Confirm booking" to "ಬುಕಿಂಗ್ ದೃಢೀಕರಿಸಿ"),
    "first_name" to ("First name" to "ಮೊದಲ ಹೆಸರು"), "last_name" to ("Last name" to "ಕೊನೆಯ ಹೆಸರು"), "dob" to ("Date of birth (YYYY-MM-DD)" to "ಜನ್ಮ ದಿನಾಂಕ (YYYY-MM-DD)"),
    "name" to ("Full name" to "ಪೂರ್ಣ ಹೆಸರು"), "phone" to ("Phone number" to "ಫೋನ್ ಸಂಖ್ಯೆ"),
    "booking_disabled" to ("Online booking is being secured and will be enabled soon." to "ಆನ್‌ಲೈನ್ ಬುಕಿಂಗ್ ಸುರಕ್ಷಿತಗೊಳ್ಳುತ್ತಿದೆ, ಶೀಘ್ರದಲ್ಲೇ ಲಭ್ಯವಾಗಲಿದೆ."),
    "booked" to ("Appointment booked." to "ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್ ಬುಕ್ ಆಗಿದೆ."),
    "sign_in_title" to ("Patient sign in" to "ರೋಗಿ ಸೈನ್ ಇನ್"), "email" to ("Email address" to "ಇಮೇಲ್ ವಿಳಾಸ"),
    "password" to ("Password" to "ಪಾಸ್‌ವರ್ಡ್"), "sign_in" to ("Sign in securely" to "ಸುರಕ್ಷಿತವಾಗಿ ಸೈನ್ ಇನ್"),
    "sign_up" to ("Create account" to "ಖಾತೆ ತೆರೆಯಿರಿ"), "have_account" to ("I already have an account" to "ನನಗೆ ಈಗಾಗಲೇ ಖಾತೆ ಇದೆ"),
    "forgot" to ("Forgot password?" to "ಪಾಸ್‌ವರ್ಡ್ ಮರೆತಿರಾ?"), "sign_out" to ("Sign out" to "ಸೈನ್ ಔಟ್"),
    "reset_sent" to ("If the email exists, a reset link has been sent." to "ಇಮೇಲ್ ಇದ್ದರೆ, ರೀಸೆಟ್ ಲಿಂಕ್ ಕಳುಹಿಸಲಾಗಿದೆ."),
    "confirm_email" to ("Check your email to confirm your account, then sign in." to "ಖಾತೆ ದೃಢೀಕರಿಸಲು ಇಮೇಲ್ ಪರಿಶೀಲಿಸಿ, ನಂತರ ಸೈನ್ ಇನ್ ಮಾಡಿ."),
    "my_bookings" to ("My bookings" to "ನನ್ನ ಬುಕಿಂಗ್‌ಗಳು"), "no_bookings" to ("No bookings yet." to "ಇನ್ನೂ ಬುಕಿಂಗ್‌ಗಳಿಲ್ಲ."),
    "queue_title" to ("Live queue" to "ಲೈವ್ ಸರತಿ"),
    "queue_body" to ("Live queue data comes only from the authorized backend. Permissions remain enforced." to "ಲೈವ್ ಸರತಿ ಡೇಟಾ ಅಧಿಕೃತ ಬ್ಯಾಕೆಂಡ್‌ನಿಂದ ಮಾತ್ರ ಬರುತ್ತದೆ. ಅನುಮತಿಗಳು ಜಾರಿಯಲ್ಲಿರುತ್ತವೆ."),
    "loading" to ("Loading…" to "ಲೋಡ್ ಆಗುತ್ತಿದೆ…"), "retry" to ("Retry" to "ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ"), "back" to ("Back" to "ಹಿಂದೆ"),
    "empty" to ("Nothing found." to "ಏನೂ ಸಿಗಲಿಲ್ಲ."),
    "help_title" to ("Care Solt Help" to "ಕೇರ್ ಸೋಲ್ಟ್ ಸಹಾಯ"),
    "faq1q" to ("How do I book an appointment?" to "ಅಪಾಯಿಂಟ್‌ಮೆಂಟ್ ಹೇಗೆ ಬುಕ್ ಮಾಡುವುದು?"),
    "faq1a" to ("Choose your hospital, department, doctor and time slot, then confirm." to "ಆಸ್ಪತ್ರೆ, ವಿಭಾಗ, ವೈದ್ಯರು ಮತ್ತು ಸಮಯ ಆಯ್ಕೆಮಾಡಿ, ನಂತರ ದೃಢೀಕರಿಸಿ."),
    "faq2q" to ("Where can I see my booking status?" to "ನನ್ನ ಬುಕಿಂಗ್ ಸ್ಥಿತಿ ಎಲ್ಲಿ ನೋಡಬಹುದು?"),
    "faq2a" to ("Sign in and open the Bookings tab." to "ಸೈನ್ ಇನ್ ಮಾಡಿ ಬುಕಿಂಗ್ ಟ್ಯಾಬ್ ತೆರೆಯಿರಿ."),
    "faq3q" to ("Can this assistant give medical advice?" to "ಈ ಸಹಾಯಕ ವೈದ್ಯಕೀಯ ಸಲಹೆ ನೀಡಬಹುದೇ?"),
    "faq3a" to ("No. Contact a qualified clinician for medical advice or local emergency services for urgent help." to "ಇಲ್ಲ. ವೈದ್ಯಕೀಯ ಸಲಹೆಗೆ ವೈದ್ಯರನ್ನು, ತುರ್ತು ಸಹಾಯಕ್ಕೆ ಸ್ಥಳೀಯ ತುರ್ತು ಸೇವೆಯನ್ನು ಸಂಪರ್ಕಿಸಿ."),
    "ai_title" to ("Care Solt AI Assistant" to "ಕೇರ್ ಸೋಲ್ಟ್ AI ಸಹಾಯಕ"),
    "ai_body" to ("Optional on-device model: Gemma 3 1B (INT4), about 529 MB. Review the model terms and access requirements before downloading." to "ಐಚ್ಛಿಕ ಆನ್-ಡಿವೈಸ್ ಮಾಡೆಲ್: Gemma 3 1B (INT4), ಸುಮಾರು 529 MB. ಡೌನ್‌ಲೋಡ್ ಮಾಡುವ ಮೊದಲು ನಿಯಮಗಳನ್ನು ಪರಿಶೀಲಿಸಿ."),
    "ai_untested" to ("This model has not yet been tested on your device. Performance must be verified." to "ಈ ಮಾಡೆಲ್ ಅನ್ನು ನಿಮ್ಮ ಸಾಧನದಲ್ಲಿ ಇನ್ನೂ ಪರೀಕ್ಷಿಸಿಲ್ಲ. ಕಾರ್ಯಕ್ಷಮತೆ ಪರಿಶೀಲಿಸಬೇಕು."),
    "ai_terms" to ("Review model terms" to "ಮಾಡೆಲ್ ನಿಯಮಗಳನ್ನು ನೋಡಿ"), "ai_skip" to ("Skip for now" to "ಈಗ ಬಿಡಿ"),
    "ai_note" to ("Patient information is never sent to the AI. The rest of Care Solt works if you skip AI." to "ರೋಗಿಯ ಮಾಹಿತಿಯನ್ನು AI ಗೆ ಕಳುಹಿಸುವುದಿಲ್ಲ. AI ಬಿಟ್ಟರೂ ಉಳಿದ ಕೇರ್ ಸೋಲ್ಟ್ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತದೆ."),
    "privacy_title" to ("Your privacy comes first" to "ನಿಮ್ಮ ಗೌಪ್ಯತೆ ಮೊದಲು"),
    "privacy_body" to ("Patient records are only visible after secure sign-in and authorization." to "ರೋಗಿಯ ದಾಖಲೆಗಳು ಸುರಕ್ಷಿತ ಸೈನ್ ಇನ್ ಮತ್ತು ಅನುಮತಿಯ ನಂತರ ಮಾತ್ರ ಗೋಚರಿಸುತ್ತವೆ.")
)

fun t(key: String, lang: String): String = S[key]?.let { if (lang == "kn") it.second else it.first } ?: key

=== FILE: gradle-snippet.txt ===
// MERGE, don't duplicate: skip SUPABASE_URL / SUPABASE_PUBLISHABLE_KEY / buildConfig lines if the project already defines them.
// Required additions: the 4 dependencies + kotlin serialization plugin.
// plugins { id("org.jetbrains.kotlin.plugin.serialization") version "<your kotlin version>" }
import java.util.Properties
val localProps = Properties().apply { rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) } }

android {
  buildFeatures { buildConfig = true; compose = true }
  defaultConfig {
    buildConfigField("String", "SUPABASE_URL", "\"https://omhkmysynqmpapetmapg.supabase.co\"")
    buildConfigField("String", "SUPABASE_PUBLISHABLE_KEY", "\"${localProps.getProperty("SUPABASE_PUBLISHABLE_KEY", "")}\"")
  }
}
dependencies {
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
  implementation("androidx.compose.material:material-icons-extended")
}
