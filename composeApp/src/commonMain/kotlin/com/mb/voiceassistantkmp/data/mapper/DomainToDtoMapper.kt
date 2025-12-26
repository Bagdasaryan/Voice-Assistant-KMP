package com.mb.voiceassistantkmp.data.mapper

import com.mb.voiceassistantkmp.data.remote.dto.VitalDto
import com.mb.voiceassistantkmp.domain.model.Vital

fun Vital.toDto() = VitalDto(
    bloodPressure = this.bloodPressure,
    bloodSugar = this.bloodSugar,
    heartBeats = this.heartBeats,
    dateTimeInMillis = this.dateTimeInMillis.toString()
)
