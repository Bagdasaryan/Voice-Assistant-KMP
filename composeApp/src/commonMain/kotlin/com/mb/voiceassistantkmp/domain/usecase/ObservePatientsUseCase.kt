package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.PatientRepository

class ObservePatientsUseCase(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke() = patientRepository.observePatients()
}
