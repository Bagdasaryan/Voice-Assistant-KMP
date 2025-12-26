package com.mb.voiceassistantkmp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mb.voiceassistantkmp.data.local.dao.ActualDataDao
import com.mb.voiceassistantkmp.data.local.dao.PatientDao
import com.mb.voiceassistantkmp.data.local.dao.VitalDao
import com.mb.voiceassistantkmp.data.local.entity.ActualDataEntity
import com.mb.voiceassistantkmp.data.local.entity.PatientEntity
import com.mb.voiceassistantkmp.data.local.entity.VitalEntity

@Database(
    entities = [
        PatientEntity::class,
        VitalEntity::class,
        ActualDataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun vitalDao(): VitalDao
    abstract fun actualDataDao(): ActualDataDao
}
