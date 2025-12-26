package com.mb.voiceassistantkmp.presentation.screen_patientDetails

data class PatientDetailsScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<DetailsStateItem> = emptyList(),
    val isEmpty: Boolean = false,

    /** State for recording logic */
    val isDialogOpen: Boolean = false,
    val showActionButtons: Boolean = false,
    val accumulatedText: String = "",
    val currentPartialText: String = "",
    // val recordedText: String = if (accumulatedText.isEmpty()) currentPartialText else "$accumulatedText $currentPartialText"
) {
    val recordedText: String
        get() = if (accumulatedText.isEmpty()) {
            currentPartialText
        } else if (currentPartialText.isEmpty()) {
            accumulatedText
        } else {
            "$accumulatedText $currentPartialText"
        }
}

data class DetailsStateItem(
    val bloodPressure: String,
    val bloodSugar: String,
    val heartBeats: String
)
