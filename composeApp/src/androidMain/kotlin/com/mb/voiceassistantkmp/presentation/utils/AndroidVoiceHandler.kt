package com.mb.voiceassistantkmp.presentation.utils

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import android.Manifest.permission.RECORD_AUDIO

@Composable
fun AndroidVoiceHandler(
    content: @Composable (VoiceAssistantHandler) -> Unit
) {
    val context = LocalContext.current
    var onPermissionGrantedCallback by remember { mutableStateOf<(() -> Unit)?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGrantedCallback?.invoke()
        } else {
            Toast.makeText(context, "Microphone is not available", Toast.LENGTH_SHORT).show()
        }
    }

    val handler = remember {
        object : VoiceAssistantHandler {
            override fun recordVoice(onResult: (String) -> Unit) {
                onPermissionGrantedCallback = {
                    onResult("GRANTED")
                }
                launcher.launch(RECORD_AUDIO)
            }
        }
    }

    content(handler)
}
