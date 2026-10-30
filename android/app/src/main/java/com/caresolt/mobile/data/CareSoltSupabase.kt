package com.caresolt.mobile.data

import com.caresolt.mobile.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.plugins.Auth
import io.github.jan.supabase.postgrest.Postgrest

object CareSoltSupabase {
    val isConfigured: Boolean
        get() = SupabaseConfig.isConfigured(
            BuildConfig.SUPABASE_URL,
            BuildConfig.SUPABASE_PUBLISHABLE_KEY
        )

    val client: SupabaseClient by lazy {
        check(isConfigured) {
            "Care Solt connection is not configured on this build."
        }
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
