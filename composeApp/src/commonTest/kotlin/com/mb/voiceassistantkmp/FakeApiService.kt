package com.mb.voiceassistantkmp

import com.mb.voiceassistantkmp.data.remote.api.ApiService
import com.mb.voiceassistantkmp.data.remote.dto.AddVitalToHostResponseDto
import com.mb.voiceassistantkmp.data.remote.dto.PatientsResponseDto
import com.mb.voiceassistantkmp.data.remote.dto.VitalDto
import com.mb.voiceassistantkmp.domain.model.Vital

class FakeApiService : ApiService(
    client = TODO("Not used in fake")
) {
    var patientsResponse: PatientsResponseDto? = null
    var analyzeResponse: VitalDto? = null
    var addVitalResponse: AddVitalToHostResponseDto? = null

    var shouldThrowException = false

    override suspend fun getPatients(lastUpdateTime: Long): PatientsResponseDto {
        if (shouldThrowException) throw Exception("Network error")
        return patientsResponse ?: throw IllegalStateException("Provide patientsResponse in test")
    }

    override suspend fun analyzeText(text: String): VitalDto {
        if (shouldThrowException) throw Exception("Analysis error")
        return analyzeResponse ?: VitalDto("120/80", "5.0", "75", "1000")
    }

    override suspend fun sendNewVitalsToHost(patientId: String, vital: Vital): AddVitalToHostResponseDto {
        if (shouldThrowException) throw Exception("Upload error")
        return addVitalResponse ?: AddVitalToHostResponseDto(success = true, serverTimeInMillis = "2000")
    }
}
