package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.PatientRepository

class SyncPatientsUseCase(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke() = patientRepository.syncPatients()
}
