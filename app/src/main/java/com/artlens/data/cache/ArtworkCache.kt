package com.artlens.data.cache

import com.artlens.data.models.ArtworkResponse

class ArtworkCache {

    private val cache = HashMap<Int, ArtworkResponse>()

    fun put(artworkId: Int, artwork: ArtworkResponse) {
        cache[artworkId] = artwork
    }

    fun get(artworkId: Int): ArtworkResponse? {
        return cache[artworkId]
    }

    fun clear() {
        cache.clear()
    }

}