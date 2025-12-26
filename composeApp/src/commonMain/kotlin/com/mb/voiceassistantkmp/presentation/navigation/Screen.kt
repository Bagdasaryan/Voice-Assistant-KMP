package com.mb.voiceassistantkmp.presentation.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")

    object PatientDetails : Screen("patientDetails/{patientId}") {
        fun createRoute(patientId: String): String {
            return "patientDetails/$patientId"
        }
    }
}
