package com.mb.voiceassistantkmp.data.local.mapper

import com.mb.voiceassistantkmp.data.local.entity.ActualDataEntity
import com.mb.voiceassistantkmp.data.local.entity.PatientEntity
import com.mb.voiceassistantkmp.data.local.entity.VitalEntity
import com.mb.voiceassistantkmp.domain.model.ActualData
import com.mb.voiceassistantkmp.domain.model.Patient
import com.mb.voiceassistantkmp.domain.model.Vital

fun Patient.toEntity() = PatientEntity(
    id = this.id,
    name = this.name,
    notes = this.notes
)

fun Vital.toEntity(id: String) = VitalEntity(
    patientId = id,
    bloodPressure = this.bloodPressure,
    bloodSugar = this.bloodSugar,
    heartBeats = this.heartBeats,
    dateTimeInMillis = this.dateTimeInMillis
)

fun ActualData.toEntity() = ActualDataEntity(
    tableName = this.tableName,
    lastUpdateTime = this.lastUpdateTime
)
