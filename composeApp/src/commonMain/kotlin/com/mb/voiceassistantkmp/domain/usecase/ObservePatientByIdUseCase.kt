package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.PatientRepository

class ObservePatientByIdUseCase(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(id: String) = patientRepository.observePatientById(id)
}
