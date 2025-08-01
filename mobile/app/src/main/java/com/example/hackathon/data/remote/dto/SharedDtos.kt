package com.example.hackathon.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Модель жанра.
 * Соответствует схеме GenresModel в Swagger.
 */
data class GenreModelDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

/**
 * Модель города.
 * Соответствует схеме CityModel в Swagger.
 */
data class CityModelDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
