package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.SpeechRecognizerRepository

class StopSpeechRecognitionUseCase(
    private val speechRecognizerRepository: SpeechRecognizerRepository
) {
    operator fun invoke() = speechRecognizerRepository.stopListening()
}
