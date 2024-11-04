package com.artlens.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "likedArtworks")
data class ArtworkEntity(
    @PrimaryKey val id: Int, // Mapped from pk
    val name: String,
    val date: String,
    val technique: String,
    val dimensions: String,
    val interpretation: String,
    val advancedInfo: String,
    val image: String,
    val museumId: Int,
    val artistId: Int,
)
