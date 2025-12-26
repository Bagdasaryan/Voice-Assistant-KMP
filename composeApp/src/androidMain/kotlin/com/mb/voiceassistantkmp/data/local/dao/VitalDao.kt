package com.mb.voiceassistantkmp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mb.voiceassistantkmp.data.local.entity.VitalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalDao {
    @Query("SELECT * FROM vitals WHERE patientId = :patientId ORDER BY dateTimeInMillis DESC")
    fun observeVitalsByPatientId(patientId: String): Flow<List<VitalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVitals(vitals: List<VitalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVital(vital: VitalEntity)
}
