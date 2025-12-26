package com.mb.voiceassistantkmp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actual_data")
data class ActualDataEntity(
    @PrimaryKey
    val tableName: String,
    val lastUpdateTime: Long
)
