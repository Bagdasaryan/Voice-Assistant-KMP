package com.mb.voiceassistantkmp.presentation.screen_patientDetails

data class PatientDetailsScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackBarEvent: SnackBarEvent? = null,
    val patientName: String = "",
    val notes: String = "",
    val items: List<DetailsStateItem> = emptyList(),
    val isEmpty: Boolean = false,
    val selectedTab: PatientDetailsTabEnum = PatientDetailsTabEnum.Vitals,

    /** State for recording logic */
    val isDialogOpen: Boolean = false,
    val showActionButtons: Boolean = false,
    val accumulatedText: String = "",
    val currentPartialText: String = "",
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
    val heartBeats: String,
    val formattedTime: String
)
