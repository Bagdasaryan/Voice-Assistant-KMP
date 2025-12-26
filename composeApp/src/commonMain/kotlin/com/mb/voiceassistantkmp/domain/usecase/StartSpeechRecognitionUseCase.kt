package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.SpeechRecognizerRepository

class StartSpeechRecognitionUseCase(
    private val speechRecognizerRepository: SpeechRecognizerRepository
) {
    operator fun invoke() = speechRecognizerRepository.startListening()
}
