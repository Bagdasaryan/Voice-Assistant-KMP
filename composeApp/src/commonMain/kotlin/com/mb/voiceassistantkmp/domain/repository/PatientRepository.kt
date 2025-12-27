package com.mb.voiceassistantkmp.domain.repository

import com.mb.voiceassistantkmp.domain.model.Patient
import com.mb.voiceassistantkmp.domain.model.Vital
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    suspend fun syncPatients()
    fun observePatients(): Flow<List<Patient>>
    fun observePatientById(id: String): Flow<Patient>
    fun observeVitalsByPatientId(id: String): Flow<List<Vital>>
    suspend fun saveVital(patientId: String, vital: Vital)
}
