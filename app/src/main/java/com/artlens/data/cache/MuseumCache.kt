package com.artlens.data.cache

import com.artlens.data.models.MuseumResponse

class MuseumCache {

    private val cache = HashMap<Int, MuseumResponse>()

    fun put(museumId: Int, museum: MuseumResponse) {
        cache[museumId] = museum
    }

    fun get(museumId: Int): MuseumResponse? {
        return cache[museumId]
    }

    fun clear() {
        cache.clear()
    }
}