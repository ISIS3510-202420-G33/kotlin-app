package com.artlens.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MuseumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(museums: List<MuseumEntity>)

    @Query("SELECT * FROM museums WHERE id = :museumId")
    suspend fun getMuseumById(museumId: Int): MuseumEntity?

    @Query("SELECT * FROM museums")
    suspend fun getAllMuseums(): List<MuseumEntity>

    @Query("DELETE FROM museums")
    suspend fun clearAll()
}