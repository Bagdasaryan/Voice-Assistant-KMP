package com.mb.voiceassistantkmp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeVitalDao : VitalDao {
    val insertedVitals = mutableListOf<VitalEntity>()

    override suspend fun insertVital(vital: VitalEntity) {
        insertedVitals.add(vital)
    }

    override suspend fun insertVitals(vitals: List<VitalEntity>) {
        insertedVitals.addAll(vitals)
    }

    override fun observeVitalsByPatientId(id: String): Flow<List<VitalEntity>> {
        return flow { emit(insertedVitals.filter { it.patientId == id }) }
    }
}

interface VitalDao {
    fun observeVitalsByPatientId(patientId: String): Flow<List<VitalEntity>>

    suspend fun insertVitals(vitals: List<VitalEntity>)

    suspend fun insertVital(vital: VitalEntity)
}

data class VitalEntity(
    val id: Long = 0,
    val patientId: String,
    val bloodPressure: String,
    val bloodSugar: String,
    val heartBeats: String,
    val dateTimeInMillis: Long
)
