package com.mb.voiceassistantkmp

import app.cash.turbine.test
import com.mb.voiceassistantkmp.domain.model.Patient
import com.mb.voiceassistantkmp.domain.model.Vital
import com.mb.voiceassistantkmp.domain.repository.PatientRepository
import com.mb.voiceassistantkmp.domain.usecase.ObservePatientsUseCase
import com.mb.voiceassistantkmp.domain.usecase.SyncPatientsUseCase
import com.mb.voiceassistantkmp.presentation.screen_patients.PatientsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.collections.emptyList
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PatientsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when init then observe patients and start sync`() = runTest {
        val fakeRepository = object : PatientRepository {
            override fun observePatients(): Flow<List<Patient>> = flowOf(
                listOf(Patient("1", "Test Patient", emptyList()))
            )
            override suspend fun syncPatients() {}
            override fun observeVitalsByPatientId(id: String): Flow<List<Vital>> = flowOf(emptyList())
            override suspend fun saveVital(id: String, v: Vital) {}
        }

        val viewModel = PatientsViewModel(
            observePatients = ObservePatientsUseCase(fakeRepository),
            syncPatients = SyncPatientsUseCase(fakeRepository)
        )

        advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()

            assertEquals(1, state.items.size)
            assertEquals("Test Patient", state.items.first().title)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `when sync fails then state should contain error message`() = runTest {
        val fakeRepository = object : PatientRepository {
            override fun observePatients(): Flow<List<Patient>> = flowOf(emptyList())
            override suspend fun syncPatients() {
                throw Exception("Network Failure")
            }
            override fun observeVitalsByPatientId(id: String): Flow<List<Vital>> = flowOf(emptyList())
            override suspend fun saveVital(id: String, v: Vital) {}
        }

        val viewModel = PatientsViewModel(
            ObservePatientsUseCase(fakeRepository),
            SyncPatientsUseCase(fakeRepository)
        )

        advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertEquals("Network Failure", state.error)
            assertEquals(false, state.isLoading)
        }
    }
}
