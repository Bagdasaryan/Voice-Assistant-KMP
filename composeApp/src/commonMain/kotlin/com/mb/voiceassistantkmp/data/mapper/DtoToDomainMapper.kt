package com.mb.voiceassistantkmp.data.mapper

import com.mb.voiceassistantkmp.data.remote.dto.PatientDto
import com.mb.voiceassistantkmp.data.remote.dto.VitalDto
import com.mb.voiceassistantkmp.domain.model.Patient
import com.mb.voiceassistantkmp.domain.model.Vital

fun PatientDto.toDomain(): Patient =
    Patient(
        id = this.id,
        name = this.name,
        notes = this.notes,
        vitals = this.vitals.map { vital ->
            vital.toDomain()
        }
    )

fun VitalDto.toDomain(): Vital =
    Vital(
        bloodPressure = this.bloodPressure,
        bloodSugar = this.bloodSugar,
        heartBeats = this.heartBeats,
        dateTimeInMillis = this.dateTimeInMillis.toLong()
    )
