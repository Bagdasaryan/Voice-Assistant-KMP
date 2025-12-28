package com.mb.voiceassistantkmp.presentation.screen_patientDetails

data class SnackBarEvent(
    val message: String,
    val type: SnackBarType
)

enum class SnackBarType {
    SUCCESS, ERROR
}
