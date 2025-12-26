package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.model.Vital
import com.mb.voiceassistantkmp.domain.repository.PatientRepository

class SaveVitalUseCase(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(patientId: String, vital: Vital) =
        patientRepository.saveVital(patientId, vital)
}
