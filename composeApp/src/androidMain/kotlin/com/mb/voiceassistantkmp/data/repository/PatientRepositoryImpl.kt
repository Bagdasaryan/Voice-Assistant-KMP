package com.mb.voiceassistantkmp.data.repository

import com.mb.voiceassistantkmp.data.local.dao.ActualDataDao
import com.mb.voiceassistantkmp.data.local.dao.PatientDao
import com.mb.voiceassistantkmp.data.local.dao.VitalDao
import com.mb.voiceassistantkmp.data.local.mapper.toDomain
import com.mb.voiceassistantkmp.data.local.mapper.toEntity
import com.mb.voiceassistantkmp.data.mapper.toDomain
import com.mb.voiceassistantkmp.data.remote.api.ApiService
import com.mb.voiceassistantkmp.domain.model.ActualData
import com.mb.voiceassistantkmp.domain.model.Patient
import com.mb.voiceassistantkmp.domain.model.Vital
import com.mb.voiceassistantkmp.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PatientRepositoryImpl(
    private val api: ApiService,
    private val patientDao: PatientDao,
    private val vitalDao: VitalDao,
    private val actualDataDao: ActualDataDao
) : PatientRepository {
    override suspend fun syncPatients() {
        val lastUpdate = actualDataDao.getActualVersionByTableName("patients").firstOrNull() ?: 0L
        val respBody = api.getPatients(lastUpdate)
        val patients = respBody.data.responseBody.patients
            .map { it.toDomain() }

        patientDao.insertPatients(patients.map { it.toEntity() })
        vitalDao.insertVitals(
            patients.flatMap { p->
                p.vitals.map { it.toEntity(p.id) }
            }
        )
        actualDataDao.insertActualVersion(
            ActualData(
                tableName = "patients",
                lastUpdateTime = respBody.serverTimeInMillis.toLong()
            ).toEntity()
        )
    }

    override fun observePatients(): Flow<List<Patient>> {
        return patientDao.observePatients()
            .map { it.map { e -> e.toDomain() } }
    }

    override fun observePatientById(id: String): Flow<Patient> {
        return patientDao.observePatientById(id)
            .map { it.toDomain() }
    }

    override fun observeVitalsByPatientId(id: String): Flow<List<Vital>> {
        return vitalDao.observeVitalsByPatientId(id)
            .map { entities ->
                entities.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override suspend fun saveVital(
        patientId: String,
        vital: Vital
    ) {
        try {
            val responseBody = api.sendNewVitalsToHost(patientId, vital)

            if (responseBody.success) {
                val serverTime = responseBody.serverTimeInMillis.toLongOrNull() ?: System.currentTimeMillis()
                vitalDao.insertVital(vital.toEntity(patientId))
                actualDataDao.insertActualVersion(
                    ActualData(
                        "patients",
                        serverTime
                    ).toEntity()
                )
            } else {
                throw Exception("Server failed to process vitals")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
