package com.artlens.data.database

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
