package com.mb.voiceassistantkmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PatientsResponseDto(
    val actual: Boolean,
    val serverTimeInMillis: String,
    val data: Data
)

@Serializable
data class Data(
    val responseCode: String,
    val responseBody: ResponseBodyDto
)

@Serializable
data class ResponseBodyDto(
    val patients: List<PatientDto>
)

@Serializable
data class PatientDto(
    val id: String,
    val name: String,
    val notes: String,
    val vitals: List<VitalDto>
)

@Serializable
data class VitalDto(
    val bloodPressure: String,
    val bloodSugar: String,
    val heartBeats: String,
    val dateTimeInMillis: String
)
