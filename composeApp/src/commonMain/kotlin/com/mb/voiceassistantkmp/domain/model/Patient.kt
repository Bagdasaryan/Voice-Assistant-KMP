package com.mb.voiceassistantkmp.domain.model

data class Patient(
    val id: String,
    val name: String,
    val vitals: List<Vital>
)
