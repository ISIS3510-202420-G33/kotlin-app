package com.artlens.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArtworkEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artworkDao(): ArtworkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "artwork_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}