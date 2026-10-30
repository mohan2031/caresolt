package com.caresolt.mobile.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Placeholder UI for the optional assistant.
 *
 * Model download, inference and network access are deliberately not implemented
 * until the LiteRT-LM runtime and model are validated on the target handset.
 */
@Composable
fun AiAssistantScreen(
    kannada: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (kannada) "Care Solt AI ಸಹಾಯಕ" else "Care Solt AI Assistant",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = if (kannada) {
                "ಸ್ಥಳೀಯ AI ಮಾದರಿಯನ್ನು ಪರಿಶೀಲಿಸಿದ ನಂತರ ಇಲ್ಲಿ ಸೇರಿಸಲಾಗುತ್ತದೆ. ಈಗ ಸಾಮಾನ್ಯ Care Solt ಸೇವೆಗಳು ಎಂದಿನಂತೆ ಲಭ್ಯವಿವೆ."
            } else {
                "The on-device AI model will be added here after compatibility and safety testing. Your normal Care Solt app remains available."
            },
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = if (kannada) {
                "ಈಗ AI ಸಕ್ರಿಯವಾಗಿಲ್ಲ. ಯಾವುದೇ ಮಾದರಿ ಡೌನ್‌ಲೋಡ್ ಅಥವಾ ವೈಯಕ್ತಿಕ ಮಾಹಿತಿಯ ಹಂಚಿಕೆ ನಡೆಯುವುದಿಲ್ಲ."
            } else {
                "AI is not enabled yet. No model is downloaded and no personal information is sent."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
