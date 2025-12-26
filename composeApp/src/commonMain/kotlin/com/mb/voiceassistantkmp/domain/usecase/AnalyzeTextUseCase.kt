package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.SpeechRecognizerRepository

class AnalyzeTextUseCase(
    private val speechRecognizerRepository: SpeechRecognizerRepository
) {
    suspend operator fun invoke(text: String) = speechRecognizerRepository.analyzeText(text)
}
