package com.artlens.data.cache

import com.artlens.data.models.ArtistResponse


class ArtistCache {

    private val cache = HashMap<Int, ArtistResponse>()

    fun put(artistId: Int, artist: ArtistResponse) {
        cache[artistId] = artist
    }

    fun get(artistId: Int): ArtistResponse? {
        return cache[artistId]
    }

    fun clear() {
        cache.clear()
    }
}