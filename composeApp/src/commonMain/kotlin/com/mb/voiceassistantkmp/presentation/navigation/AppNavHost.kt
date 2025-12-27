package com.mb.voiceassistantkmp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mb.voiceassistantkmp.presentation.screen_patientDetails.PatientDetailsScreen
import com.mb.voiceassistantkmp.presentation.screen_patientDetails.PatientDetailsViewModel
import com.mb.voiceassistantkmp.presentation.screen_patients.PatientsScreen
import com.mb.voiceassistantkmp.presentation.screen_patients.PatientsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            val viewModel: PatientsViewModel = koinViewModel()
            PatientsScreen(
                viewModel = viewModel,
                onPatientClick = { id ->
                    navController.navigate(Screen.PatientDetails.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.PatientDetails.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.savedStateHandle.get<String>("patientId") ?: ""

            val viewModel: PatientDetailsViewModel = koinViewModel { parametersOf(patientId) }
            PatientDetailsScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
