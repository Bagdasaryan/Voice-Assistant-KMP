package com.mb.voiceassistantkmp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.collections.toMutableMap

class FakeActualDataDao : ActualDataDao {
    private val actualDataState = MutableStateFlow<Map<String, ActualDataEntity>>(emptyMap())

    val lastSavedVersion: Long?
        get() = actualDataState.value["patients"]?.lastUpdateTime

    override fun getActualVersionByTableName(tableName: String): Flow<Long> {
        return actualDataState.map { it[tableName]?.lastUpdateTime ?: 0L }
    }

    override suspend fun insertActualVersion(actualData: ActualDataEntity) {
        actualDataState.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            newMap[actualData.tableName] = actualData
            newMap
        }
    }
}

interface ActualDataDao {
    fun getActualVersionByTableName(tableName: String): Flow<Long>

    suspend fun insertActualVersion(actualData: ActualDataEntity)
}

data class ActualDataEntity(
    val tableName: String,
    val lastUpdateTime: Long
)
