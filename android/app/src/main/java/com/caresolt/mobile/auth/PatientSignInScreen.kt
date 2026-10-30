package com.caresolt.mobile.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.caresolt.mobile.data.CareSoltSupabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

@Composable
fun PatientSignInScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Patient sign in", style = MaterialTheme.typography.headlineSmall)
        Text("Use the email and password for your existing Care Solt patient account.")
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; message = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email address") },
            singleLine = true
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; message = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Button(
            onClick = {
                if (!CareSoltSupabase.isConfigured) {
                    message = "This build is not configured yet. Add SUPABASE_URL and SUPABASE_PUBLISHABLE_KEY to android/local.properties, then rebuild."
                } else if (email.isBlank() || password.isBlank()) {
                    message = "Enter your email and password."
                } else {
                    scope.launch {
                        isLoading = true
                        message = null
                        try {
                            CareSoltSupabase.client.auth.signInWith(Email) {
                                this.email = email.trim()
                                this.password = password
                            }
                            message = "Signed in. Your account is authenticated."
                        } catch (error: Exception) {
                            message = "Sign-in failed. Check your details and internet connection."
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator()
            else Text("Sign in securely")
        }
        message?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
