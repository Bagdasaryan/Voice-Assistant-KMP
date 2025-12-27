package com.mb.voiceassistantkmp.presentation.screen_patients

data class PatientsScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val items: List<PatientStateItem> = emptyList(),
    val isEmpty: Boolean = false
)

data class PatientStateItem(
    val id: String,
    val title: String,
    val subtitle: String
)
