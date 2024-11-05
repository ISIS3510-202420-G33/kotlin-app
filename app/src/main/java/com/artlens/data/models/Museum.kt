package com.artlens.data.models

import com.artlens.data.database.MuseumEntity

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

// Conversión de MuseumResponse a MuseumEntity para almacenamiento
fun MuseumResponse.toMuseumEntity(): MuseumEntity {
    return MuseumEntity(
        id = this.pk,
        name = this.fields.name,
        latitude = this.fields.latitude,
        longitude = this.fields.longitude,
        category = this.fields.category,
        city = this.fields.city,
        country = this.fields.country,
        description = this.fields.description,
        image = this.fields.image
    )
}

fun MuseumEntity.toMuseumResponse(): MuseumResponse {
    return MuseumResponse(
        pk = this.id,
        fields = MuseumFields(
            name = this.name,
            latitude = this.latitude,
            longitude = this.longitude,
            category = this.category,
            city = this.city,
            country = this.country,
            description = this.description,
            image = this.image
        )
    )
}
