package com.mb.voiceassistantkmp.presentation.screen_patientDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mb.voiceassistantkmp.domain.model.Vital
import com.mb.voiceassistantkmp.domain.usecase.AnalyzeTextUseCase
import com.mb.voiceassistantkmp.domain.usecase.ObserveVitalsUseCase
import com.mb.voiceassistantkmp.domain.usecase.SaveVitalUseCase
import com.mb.voiceassistantkmp.domain.usecase.StartSpeechRecognitionUseCase
import com.mb.voiceassistantkmp.domain.usecase.StopSpeechRecognitionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientDetailsViewModel(
    private val patientId: String,
    private val observePatients: ObserveVitalsUseCase,
    private val startSpeechRecognition: StartSpeechRecognitionUseCase,
    private val stopSpeechRecognition: StopSpeechRecognitionUseCase,
    private val analyzeTextUseCase: AnalyzeTextUseCase,
    private val saveVitalUseCase: SaveVitalUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PatientDetailsScreenState())
    val state = _state.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    private var recognitionJob: Job? = null

    init {
        observePatientsFoo(patientId)
    }

    fun onRecordButtonClick() {
        _state.update {
            if (!it.isDialogOpen) {
                startRecording()
                it.copy(
                    isDialogOpen = true
                )
            } else {
                if (it.showActionButtons) {
                    startRecording()
                    it.copy(
                        showActionButtons = false,
                        accumulatedText = "",
                        currentPartialText = ""
                    )
                } else {
                    stopRecording()
                    it.copy(showActionButtons = true)
                }
            }
        }
    }

    fun onDismissRecording() {
        _state.update {
            it.copy(
                isDialogOpen = false,
                showActionButtons = false,
                accumulatedText = "",
                currentPartialText = ""
            )
        }
    }

    fun onConfirmRecording() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                analyzeTextUseCase(_state.value.accumulatedText).apply {
                    saveVitalUseCase(patientId = patientId, vital = Vital(
                        bloodPressure = bloodPressure,
                        bloodSugar = bloodSugar,
                        heartBeats = heartBeats,
                        dateTimeInMillis = dateTimeInMillis
                    ))
                    _state.update { it.copy(
                        isLoading = false,
                        isDialogOpen = false,
                        showActionButtons = false,
                        accumulatedText = "",
                        currentPartialText = ""
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    isDialogOpen = false,
                    showActionButtons = false,
                    error = e.message
                ) }
            }
        }
    }

    private fun observePatientsFoo(patientId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            observePatients(patientId)
                .collect { vitals ->
                    val uiItems = vitals.map {
                        DetailsStateItem(
                            bloodPressure = it.bloodPressure,
                            bloodSugar = it.bloodSugar,
                            heartBeats = it.heartBeats
                        )
                    }
                    _state.update { it.copy(
                        items = uiItems,
                        isLoading = false
                    ) }
                }
        }
    }

    private fun startRecording() {
        _isRecording.value = true
        recognitionJob?.cancel()

        var textFromThisSession = ""
        recognitionJob = viewModelScope.launch {
            try {
                startSpeechRecognition()
                    .collect { currentPartialText ->
                        textFromThisSession = currentPartialText
                        _state.update { it.copy(
                            currentPartialText = currentPartialText
                        ) }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message
                ) }
            } finally {
                if (textFromThisSession.isNotBlank()) {
                    _state.update { currentState ->
                        val newAccumulated = if (currentState.accumulatedText.isBlank()) {
                            textFromThisSession
                        } else {
                            "${currentState.accumulatedText} $textFromThisSession"
                        }
                        currentState.copy(
                            accumulatedText = newAccumulated.trim(),
                            currentPartialText = ""
                        )
                    }
                }

                if (_isRecording.value) {
                    startRecording()
                }
            }
        }
    }

    private fun stopRecording() {
        _isRecording.value = false
        recognitionJob?.cancel()
        stopSpeechRecognition()
        _state.update { it.copy(
            accumulatedText = _state.value.accumulatedText,
            currentPartialText = ""
        ) }
    }
}
