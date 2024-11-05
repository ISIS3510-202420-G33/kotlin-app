package com.artlens.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "museums")
data class MuseumEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val city: String,
    val country: String,
    val description: String,
    val image: String
)
