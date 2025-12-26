package com.mb.voiceassistantkmp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    val id: String,
    val name: String
)
