package com.mb.voiceassistantkmp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePatientDao : PatientDao {
    private val patientsState = MutableStateFlow<Map<String, PatientEntity>>(emptyMap())

    override fun observePatients(): Flow<List<PatientEntity>> {
        return patientsState.map { it.values.toList() }
    }

    override suspend fun insertPatients(patients: List<PatientEntity>) {
        patientsState.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            patients.forEach { patient ->
                newMap[patient.id] = patient
            }
            newMap
        }
    }
}

interface PatientDao {
    fun observePatients(): Flow<List<PatientEntity>>
    suspend fun insertPatients(patients: List<PatientEntity>)
}

data class PatientEntity(
    val id: String,
    val name: String
)
