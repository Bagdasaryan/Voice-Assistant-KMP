package com.mb.voiceassistantkmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddVitalToHostResponseDto(
    val success: Boolean,
    val serverTimeInMillis: String
)
