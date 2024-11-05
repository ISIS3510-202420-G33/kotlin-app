package com.artlens.data.database

class MuseumRepository(private val museumDao: MuseumDao) {

    suspend fun insert(museums: List<MuseumEntity>) {
        museumDao.insertAll(museums)
    }

    suspend fun getAllMuseums(): List<MuseumEntity> {
        return museumDao.getAllMuseums()
    }

    suspend fun getMuseumById(museumId: Int): MuseumEntity? {
        return museumDao.getMuseumById(museumId)
    }

    suspend fun deleteAllMuseums() {
        museumDao.clearAll()
    }
}
