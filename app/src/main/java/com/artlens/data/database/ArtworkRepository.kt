package com.artlens.data.database

class ArtworkRepository(private val artworkDao: ArtworkDao) {

    suspend fun insert(artwork: List<ArtworkEntity>) {
        artworkDao.insertArtworks(artwork)
    }

    suspend fun getAllArtworks(): List<ArtworkEntity> {
        return artworkDao.getLikedArtworks()
    }

    suspend fun deleteAll() {
        artworkDao.deleteAllArtworks()
    }
}