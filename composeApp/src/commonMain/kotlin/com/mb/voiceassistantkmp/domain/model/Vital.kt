package com.mb.voiceassistantkmp.domain.model

data class Vital(
    val bloodPressure: String,
    val bloodSugar: String,
    val heartBeats: String,
    val dateTimeInMillis: Long
) {
    val hasEmptyFields: Boolean
        get() = bloodPressure.isBlank() || bloodSugar.isBlank() || heartBeats.isBlank()
}
