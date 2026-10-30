package com.caresolt.mobile.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/** Tokens live only in EncryptedSharedPreferences. */
class SessionStore(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context, "caresolt_secure",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    var accessToken: String? get() = prefs.getString("at", null); set(v) = prefs.edit().putString("at", v).apply()
    var refreshToken: String? get() = prefs.getString("rt", null); set(v) = prefs.edit().putString("rt", v).apply()
    var userId: String? get() = prefs.getString("uid", null); set(v) = prefs.edit().putString("uid", v).apply()
    var email: String? get() = prefs.getString("em", null); set(v) = prefs.edit().putString("em", v).apply()
    var lang: String get() = prefs.getString("lang", "en") ?: "en"; set(v) = prefs.edit().putString("lang", v).apply()
    fun clearSession() { prefs.edit().remove("at").remove("rt").remove("uid").remove("em").apply() }
}
