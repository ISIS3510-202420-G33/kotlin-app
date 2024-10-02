package com.artlens.data.models

data class ArtworkResponse(
    val pk: Int,
    val fields: ArtworkFields
)

data class ArtworkFields(
    val name: String,
    val date: String,
    val technique: String,
    val dimensions: String,
    val interpretation: String,
    val advancedInfo: String,
    val image: String,
    val museum: Int,
    val artist: Int
)