package com.mb.voiceassistantkmp.domain.repository

import com.mb.voiceassistantkmp.domain.model.Vital
import kotlinx.coroutines.flow.Flow

interface SpeechRecognizerRepository {
    suspend fun analyzeText(text: String): Vital
    fun startListening(): Flow<String>
    fun stopListening()
}
