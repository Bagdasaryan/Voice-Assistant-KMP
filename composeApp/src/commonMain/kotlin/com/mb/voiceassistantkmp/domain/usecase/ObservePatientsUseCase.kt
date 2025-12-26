package com.mb.voiceassistantkmp.domain.usecase

import com.mb.voiceassistantkmp.domain.repository.PatientRepository

open class ObservePatientsUseCase(
    private val patientRepository: PatientRepository
) {
    open suspend operator fun invoke() = patientRepository.observePatients()
}
