package com.mb.voiceassistantkmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetPatientsRequestDto(
    val lastUpdateTime: String
)
