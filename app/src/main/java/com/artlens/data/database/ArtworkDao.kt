package com.artlens.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtworkDao {

    @Query("SELECT * FROM likedArtworks" )
    suspend fun getLikedArtworks(): List<ArtworkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtworks(artworks: List<ArtworkEntity>)

    @Query("DELETE FROM likedArtworks")
    suspend fun deleteAllArtworks()
}

