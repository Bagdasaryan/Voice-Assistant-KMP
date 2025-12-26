package com.mb.voiceassistantkmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddVitalToHostRequestDto(
    val patientId: String,
    val vital: VitalDto
)
