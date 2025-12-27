package com.mb.voiceassistantkmp.data.remote.api

import com.mb.voiceassistantkmp.data.mapper.toDto
import com.mb.voiceassistantkmp.data.remote.dto.AddVitalToHostRequestDto
import com.mb.voiceassistantkmp.data.remote.dto.AddVitalToHostResponseDto
import com.mb.voiceassistantkmp.data.remote.dto.GetPatientsRequestDto
import com.mb.voiceassistantkmp.data.remote.dto.ParseTextRequestDto
import com.mb.voiceassistantkmp.data.remote.dto.PatientsResponseDto
import com.mb.voiceassistantkmp.data.remote.dto.VitalDto
import com.mb.voiceassistantkmp.data.remote.endpoint.Endpoints
import com.mb.voiceassistantkmp.domain.model.Vital
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

open class ApiService(
    private val client: HttpClient,
    private val endpoint: Endpoints
) {
    open suspend fun getPatients(lastUpdateTime: Long): PatientsResponseDto {
        return client.post(urlString = "${endpoint.urlStr}/patients") {
            contentType(ContentType.Application.Json)
            setBody(GetPatientsRequestDto(lastUpdateTime = lastUpdateTime.toString()))
        }.body()
    }

    open suspend fun analyzeText(text: String): VitalDto {
        return client.post("${endpoint.urlStr}/parse") {
            contentType(ContentType.Application.Json)
            setBody(ParseTextRequestDto(text = text))
        }.body()
    }

    open suspend fun sendNewVitalsToHost(patientId: String, vital: Vital): AddVitalToHostResponseDto {
        return client.post("${endpoint.urlStr}/patients/vital") {
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
