package com.caresolt.mobile.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SupabaseConfigTest {
    @Test
    fun missingUrlIsNotConfigured() {
        assertFalse(SupabaseConfig.isConfigured("", "sb_publishable_test"))
    }

    @Test
    fun missingPublishableKeyIsNotConfigured() {
        assertFalse(SupabaseConfig.isConfigured("https://example.supabase.co", ""))
    }

    @Test
    fun validProjectUrlAndPublishableKeyAreConfigured() {
        assertTrue(
            SupabaseConfig.isConfigured(
                "https://example.supabase.co",
                "sb_publishable_test"
            )
        )
    }

    @Test
    fun rejectsNonSupabaseUrl() {
        assertFalse(SupabaseConfig.isConfigured("https://example.com", "sb_publishable_test"))
    }
}
