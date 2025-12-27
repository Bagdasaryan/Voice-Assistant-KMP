package com.mb.voiceassistantkmp.di

import com.mb.voiceassistantkmp.domain.usecase.AnalyzeTextUseCase
import com.mb.voiceassistantkmp.domain.usecase.ObservePatientByIdUseCase
import com.mb.voiceassistantkmp.domain.usecase.ObservePatientsUseCase
import com.mb.voiceassistantkmp.domain.usecase.ObserveVitalsUseCase
import com.mb.voiceassistantkmp.domain.usecase.SaveVitalUseCase
import com.mb.voiceassistantkmp.domain.usecase.StartSpeechRecognitionUseCase
import com.mb.voiceassistantkmp.domain.usecase.StopSpeechRecognitionUseCase
import com.mb.voiceassistantkmp.domain.usecase.SyncPatientsUseCase
import com.mb.voiceassistantkmp.presentation.screen_patientDetails.PatientDetailsViewModel
import com.mb.voiceassistantkmp.presentation.screen_patients.PatientsViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

fun commonModule() = module {
    factory { ObservePatientsUseCase(get()) }
    factory { SyncPatientsUseCase(get()) }
    factory { ObserveVitalsUseCase(get()) }
    factory { ObservePatientByIdUseCase(get()) }
    factory { StartSpeechRecognitionUseCase(get()) }
    factory { StopSpeechRecognitionUseCase(get()) }
    factory { AnalyzeTextUseCase(get()) }
    factory { SaveVitalUseCase(get()) }

    viewModel<PatientsViewModel> { PatientsViewModel(get(), get()) }
    viewModel<PatientDetailsViewModel> { (patientId: String) ->
        PatientDetailsViewModel(
            patientId,
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
