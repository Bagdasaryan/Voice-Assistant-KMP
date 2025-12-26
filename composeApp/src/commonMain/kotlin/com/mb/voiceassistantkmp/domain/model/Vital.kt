package com.mb.voiceassistantkmp.domain.model

data class Vital(
    val bloodPressure: String,
    val bloodSugar: String,
    val heartBeats: String,
    val dateTimeInMillis: Long
)
