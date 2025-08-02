package com.example.hackathon.data.remote.dto

import com.google.gson.annotations.SerializedName

// Модель для создания книги (запрос)
data class BookCreateRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("extra_terms") val extra_terms: String?,
    @SerializedName("author_id") val authorId: Int,
    @SerializedName("genre_id") val genreId: Int,
    @SerializedName("language") val language: String,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("condition") val condition: String,
    @SerializedName("exchange_location_id") val exchangeLocationId: Int,
    @SerializedName("is_available") val isAvailable: Boolean = true
)

// Модель книги (ответ от сервера)
data class BookModelDto(
    @SerializedName("id") val id: String,
    @SerializedName("owner_id") val ownerId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("extra_terms") val extraTerms: String?,
    @SerializedName("author") val author: AuthorModelDto,
    @SerializedName("genre") val genre: GenreModelDto,
    @SerializedName("language") val language: String,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("condition") val condition: String,
    @SerializedName("photo_urls") val photoUrls: List<String>,
    @SerializedName("exchange_location") val exchangeLocation: ExchangeLocationDto,
    @SerializedName("is_available") val isAvailable: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?
)

// Модель автора
data class AuthorModelDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

// Модель жанра
data class GenreModelDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

// Модель города
data class CityModelDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

// Модель места обмена
data class ExchangeLocationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("city") val city: CityModelDto,
    @SerializedName("is_active") val isActive: Boolean
)

// Enum для состояния книги
enum class Condition {
    @SerializedName("new") NEW,
    @SerializedName("perfect") PERFECT,
    @SerializedName("good") GOOD,
    @SerializedName("normal") NORMAL
}
