package com.mb.voiceassistantkmp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mb.voiceassistantkmp.data.local.entity.ActualDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActualDataDao {
    @Query("SELECT lastUpdateTime FROM actual_data WHERE tableName = :tableName")
    fun getActualVersionByTableName(tableName: String): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActualVersion(actualData: ActualDataEntity)
}
