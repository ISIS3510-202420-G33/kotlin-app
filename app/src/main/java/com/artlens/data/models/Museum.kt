package com.artlens.data.models

data class MuseumResponse(
    val pk: Int,
    val fields: MuseumFields
)

data class MuseumFields(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val city: String,
    val country: String,
    val description: String,
    val image: String
)