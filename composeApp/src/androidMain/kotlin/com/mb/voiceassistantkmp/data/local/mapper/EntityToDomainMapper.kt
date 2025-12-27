package com.mb.voiceassistantkmp.data.local.mapper

import com.mb.voiceassistantkmp.data.local.entity.ActualDataEntity
import com.mb.voiceassistantkmp.data.local.entity.PatientEntity
import com.mb.voiceassistantkmp.data.local.entity.VitalEntity
import com.mb.voiceassistantkmp.domain.model.ActualData
import com.mb.voiceassistantkmp.domain.model.Patient
import com.mb.voiceassistantkmp.domain.model.Vital

fun PatientEntity.toDomain() = Patient(
    id = this.id,
    name = this.name,
    notes = this.notes,
    vitals = emptyList()
)

fun VitalEntity.toDomain() = Vital(
    bloodPressure = this.bloodPressure,
    bloodSugar = this.bloodSugar,
    heartBeats = this.heartBeats,
    dateTimeInMillis = this.dateTimeInMillis
)

fun ActualDataEntity.toDomain() = ActualData(
    tableName = this.tableName,
    lastUpdateTime = this.lastUpdateTime
)
