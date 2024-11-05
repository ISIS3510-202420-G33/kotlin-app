package com.artlens.data.database

import com.artlens.data.models.toMuseumEntity

class MuseumRepository(private val museumDao: MuseumDao) {

    suspend fun insert(museum: List<MuseumEntity>) {
        museumDao.insertMuseums(museum)
    }

    suspend fun getAllMuseums(): List<MuseumEntity> {
        return museumDao.getAllMuseums()
    }

    suspend fun deleteAll() {
        museumDao.deleteAllMuseums()
    }
}
