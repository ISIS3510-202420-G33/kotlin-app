package com.artlens.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MuseumDao {

    @Query("SELECT * FROM museums")
    suspend fun getAllMuseums(): List<MuseumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuseums(museums: List<MuseumEntity>)

    @Query("DELETE FROM museums")
    suspend fun deleteAllMuseums()
}
