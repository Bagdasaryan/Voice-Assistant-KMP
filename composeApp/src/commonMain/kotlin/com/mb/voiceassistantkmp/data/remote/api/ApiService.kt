package com.mb.voiceassistantkmp.data.remote.api

import com.mb.voiceassistantkmp.data.mapper.toDto
import com.mb.voiceassistantkmp.data.remote.dto.AddVitalToHostRequestDto
import com.mb.voiceassistantkmp.data.remote.dto.AddVitalToHostResponseDto
import com.mb.voiceassistantkmp.data.remote.dto.GetPatientsRequestDto
import com.mb.voiceassistantkmp.data.remote.dto.ParseTextRequestDto
import com.mb.voiceassistantkmp.data.remote.dto.PatientsResponseDto
import com.mb.voiceassistantkmp.data.remote.dto.VitalDto
import com.mb.voiceassistantkmp.domain.model.Vital
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(
    private val client: HttpClient
) {
    // https://raw.githubusercontent.com/Bagdasaryan/mock_server/main/list_of_all_patients.js
    // http://192.168.178.175:5000
    // http://172.20.10.2:5000

    suspend fun getPatients(lastUpdateTime: Long): PatientsResponseDto {
        return client.post(urlString = "http://192.168.178.175:5000/patients") {
            contentType(ContentType.Application.Json)
            setBody(GetPatientsRequestDto(lastUpdateTime = lastUpdateTime.toString()))
        }.body()
    }

    suspend fun analyzeText(text: String): VitalDto {
        return client.post("http://192.168.178.175:5000/parse") {
            contentType(ContentType.Application.Json)
            setBody(ParseTextRequestDto(text = text))
        }.body()
    }

    suspend fun sendNewVitalsToHost(patientId: String, vital: Vital): AddVitalToHostResponseDto {
        return client.post("http://192.168.178.175:5000/patients/vital") {
            contentType(ContentType.Application.Json)
            setBody(
                AddVitalToHostRequestDto(
                    patientId = patientId,
                    vital = vital.toDto()
                )
            )
        }.body()
    }
}
