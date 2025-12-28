package com.mb.voiceassistantkmp.presentation.screen_patientDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mb.voiceassistantkmp.domain.usecase.AnalyzeTextUseCase
import com.mb.voiceassistantkmp.domain.usecase.ObservePatientByIdUseCase
import com.mb.voiceassistantkmp.domain.usecase.ObserveVitalsUseCase
import com.mb.voiceassistantkmp.domain.usecase.SaveVitalUseCase
import com.mb.voiceassistantkmp.domain.usecase.StartSpeechRecognitionUseCase
import com.mb.voiceassistantkmp.domain.usecase.StopSpeechRecognitionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class PatientDetailsViewModel(
    private val patientId: String,
    private val observeVitals: ObserveVitalsUseCase,
    private val observePatient: ObservePatientByIdUseCase,
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
                val result = analyzeTextUseCase(_state.value.accumulatedText)

                _state.update { it.copy(
                    isLoading = false,
                    isDialogOpen = false,
                    showActionButtons = false
                ) }
                if (result.hasEmptyFields) {
                    _state.update { it.copy(
                        snackBarEvent = SnackBarEvent(
                            message = "Some data was not recognized. Please try again.",
                            type = SnackBarType.ERROR
                        )
                    ) }
                } else {
                    saveVitalUseCase(patientId, result)
                    _state.update { it.copy(
                        snackBarEvent = SnackBarEvent(
                            message = "Vitals saved successfully!",
                            type = SnackBarType.SUCCESS
                        )
                    ) }
                }

                _state.update { it.copy(accumulatedText = "", currentPartialText = "") }
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

    fun onTabSelected(tab: PatientDetailsTabEnum) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onSnackBarShown() {
        _state.update { it.copy(snackBarEvent = null) }
    }

    @OptIn(ExperimentalTime::class)
    private fun observePatientsFoo(patientId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            combine(
                observePatient(patientId),
                observeVitals(patientId)
            ) { patient, vitals ->
                val uiItems = vitals.map {
                    val instant = Instant.fromEpochMilliseconds(it.dateTimeInMillis)
                    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    val day = localDateTime.day.toString().padStart(2, '0')
                    val month = localDateTime.month.number.toString().padStart(2, '0')
                    val year = localDateTime.year
                    val hour = localDateTime.hour.toString().padStart(2, '0')
                    val minute = localDateTime.minute.toString().padStart(2, '0')
                    val formattedTime = "$day.$month.$year $hour:$minute"

                    DetailsStateItem(
                        bloodPressure = it.bloodPressure,
                        bloodSugar = it.bloodSugar,
                        heartBeats = it.heartBeats,
                        formattedTime = formattedTime
                    )
                }

                _state.update {
                    it.copy(
                        patientName = patient.name,
                        notes = patient.notes,
                        items = uiItems,
                        isLoading = false
                    )
                }
            }.collect()
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
