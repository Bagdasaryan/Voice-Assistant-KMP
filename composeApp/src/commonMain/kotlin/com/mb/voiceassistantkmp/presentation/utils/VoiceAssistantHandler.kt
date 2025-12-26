package com.mb.voiceassistantkmp.presentation.utils

import androidx.compose.runtime.staticCompositionLocalOf

interface VoiceAssistantHandler {
    fun recordVoice(onResult: (String) -> Unit)
}

val LocalVoiceHandler = staticCompositionLocalOf<VoiceAssistantHandler> {
    error("No VoiceAssistantHandler provided")
}
