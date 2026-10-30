package com.caresolt.mobile.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import java.io.File

private const val GEMMA_MODEL_PAGE = "https://huggingface.co/litert-community/Gemma3-1B-IT"

@Composable
fun AiAssistantScreen(
    kannada: Boolean,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val modelFile = remember(context) {
        File(context.filesDir, "models/gemma3-1b-it-int4.litertlm")
    }
    val modelFilePresent = remember(modelFile) { ModelReadiness.isReady(modelFile) }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (kannada) "Care Solt AI ಸಹಾಯಕ" else "Care Solt AI Assistant",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = if (kannada) {
                "ಐಚ್ಛಿಕ ಸ್ಥಳೀಯ AI ಮಾದರಿ: Gemma 3 1B (INT4). ಅಂದಾಜು ಮಾದರಿ ಗಾತ್ರ 529 MB. ಡೌನ್‌ಲೋಡ್ ಮಾಡುವ ಮೊದಲು ಮಾದರಿಯ ನಿಯಮಗಳು ಮತ್ತು ಅನುಮತಿಯನ್ನು ಪರಿಶೀಲಿಸಿ."
            } else {
                "Optional on-device model: Gemma 3 1B (INT4), approximately 529 MB. Review the model terms and access requirements before downloading."
            },
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = if (kannada) {
                "ಮಾದರಿ ನಿಮ್ಮ OPPO ಫೋನ್‌ನಲ್ಲಿ ಸರಿಯಾಗಿ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತದೆ ಎಂದು ಇನ್ನೂ ಪರೀಕ್ಷಿಸಿಲ್ಲ. CPU/GPU ಕಾರ್ಯಕ್ಷಮತೆಯನ್ನು ಸಾಧನದಲ್ಲೇ ಪರಿಶೀಲಿಸಬೇಕು."
            } else {
                "This model has not yet been tested on your OPPO phone. CPU/GPU performance must be verified on the actual device."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Button(
            onClick = { uriHandler.openUri(GEMMA_MODEL_PAGE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (kannada) "ಮಾದರಿ ನಿಯಮಗಳನ್ನು ಪರಿಶೀಲಿಸಿ" else "Review model terms")
        }
        OutlinedButton(
            onClick = { /* Keep AI disabled when skipped. */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (kannada) "ಈಗ ಬೇಡ — AI ಬಿಟ್ಟುಬಿಡಿ" else "Skip for now")
        }
        Text(
            text = if (modelFilePresent) {
                if (kannada) "ಮಾದರಿ ಫೈಲ್ ಕಂಡುಬಂದಿದೆ. ಅದರ ಸಮಗ್ರತೆ ಮತ್ತು ಸಾಧನ ಹೊಂದಾಣಿಕೆ ಪರಿಶೀಲನೆಯ ನಂತರ ಮಾತ್ರ AI ಲೋಡ್ ಮಾಡಲಾಗುತ್ತದೆ."
                else "A non-empty model file was found. AI remains disabled until its integrity and device compatibility are verified."
            } else {
                if (kannada) "ಮಾದರಿ ಇನ್ನೂ ಇನ್‌ಸ್ಟಾಲ್ ಆಗಿಲ್ಲ. ಈಗ ಯಾವುದೇ ಡೌನ್‌ಲೋಡ್ ನಡೆಯುವುದಿಲ್ಲ."
                else "Model is not installed yet. No download is started at this stage."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = if (kannada) {
                "ರೋಗಿಯ ಮಾಹಿತಿ AI ಗೆ ಕಳುಹಿಸುವುದಿಲ್ಲ. AI ಬಿಟ್ಟರೂ Care Solt ನ ಇತರ ಸೇವೆಗಳು ಲಭ್ಯ."
            } else {
                "Patient information is never sent to the AI. The rest of Care Solt remains available if you skip AI."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
