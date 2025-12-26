package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.PatientRepository

open class SyncPatientsUseCase(
    private val patientRepository: PatientRepository
) {
    open suspend operator fun invoke() = patientRepository.syncPatients()
}
