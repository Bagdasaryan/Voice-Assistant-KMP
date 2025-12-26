package com.mb.voiceassistantkmp.presentation.screen_patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mb.voiceassistantkmp.domain.usecase.ObservePatientsUseCase
import com.mb.voiceassistantkmp.domain.usecase.SyncPatientsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientsViewModel(
    private val observePatients: ObservePatientsUseCase,
    private val syncPatients: SyncPatientsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PatientsScreenState())
    val state = _state.asStateFlow()

    init {
        observePatientsFoo()
        syncDataFoo()
    }

    private fun observePatientsFoo() {
        viewModelScope.launch {
            observePatients()
                .collect { patients ->
                    val uiItems = patients.map { patient ->
                        PatientStateItem(
                            id = patient.id,
                            title = patient.name,
                            subtitle = "Click to check details"
                        )
                    }

                    _state.update { it.copy(
                        items = uiItems,
                        isEmpty = uiItems.isEmpty(),
                        isLoading = false
                    ) }
                }
        }
    }

    private fun syncDataFoo() {
        viewModelScope.launch {
            if (_state.value.items.isEmpty()) {
                _state.update { it.copy(isLoading = true) }
            }

            try {
                syncPatients()
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message,
                    isLoading = false
                ) }
            }
        }
    }
}
