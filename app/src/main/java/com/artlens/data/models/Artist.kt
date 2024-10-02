package com.artlens.data.models

data class ArtistResponse(
    val pk: Int,
    val fields: ArtistFields
)

data class ArtistFields(
    val name: String,
    val biography: String,
    val image: String
)
