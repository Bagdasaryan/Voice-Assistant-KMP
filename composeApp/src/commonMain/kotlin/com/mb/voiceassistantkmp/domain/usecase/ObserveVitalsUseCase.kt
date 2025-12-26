package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.PatientRepository

class ObserveVitalsUseCase(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(id: String) = patientRepository.observeVitalsByPatientId(id)
}
