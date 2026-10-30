package com.caresolt.mobile.data

import java.net.URI

object SupabaseConfig {
    fun isConfigured(url: String, publishableKey: String): Boolean {
        val parsed = runCatching { URI(url.trim()) }.getOrNull() ?: return false
        val validHost = parsed.host?.endsWith(".supabase.co") == true
        val validKey = publishableKey.startsWith("sb_publishable_") ||
            publishableKey.startsWith("eyJ")
        return parsed.scheme == "https" && validHost && validKey
    }
}
