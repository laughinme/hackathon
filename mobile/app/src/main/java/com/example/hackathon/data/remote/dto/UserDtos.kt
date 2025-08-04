package com.example.hackathon.data.remote.dto

import com.example.hackathon.domain.model.UserProfile
import com.google.gson.annotations.SerializedName

/**
 * Модель пользователя, получаемая от API.
 * Соответствует схеме UserModel в Swagger.
 */
data class UserModelDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("created_at")
    val createdAt: String, // Формат "date-time"
    @SerializedName("updated_at")
    val updatedAt: String?, // Формат "date-time"
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("birth_date")
    val birthDate: String?, // Формат "date"
    @SerializedName("age")
    val age: Int?,
    @SerializedName("gender")
    val gender: String?, // "male", "female", "unknown"
    @SerializedName("language")
    val language: String?,
    @SerializedName("favorite_genres")
    val favoriteGenres: List<GenreModelDto>,
    @SerializedName("city")
    val city: CityModelDto?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("is_onboarded")
    val isOnboarded: Boolean,
    @SerializedName("banned")
    val isBanned: Boolean,
    @SerializedName("public")
    val isPublic: Boolean
)

/**
 * Тело запроса для обновления профиля пользователя.
 * Соответствует схеме UserPatch в Swagger.
 */
data class UserPatchRequest(
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("birth_date")
    val birthDate: String? = null, // Формат "date"
    @SerializedName("gender")
    val gender: String? = null, // "male", "female", "unknown"
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("city_id")
    val cityId: Int? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("public")
    val isPublic: Boolean? = null
)

/**
 * Тело запроса для обновления любимых жанров.
 * Соответствует схеме GenresPatch в Swagger.
 */
data class GenresPatchRequest(
    @SerializedName("favorite_genres")
    val favoriteGenres: List<Int>
)


fun UserModelDto.toDomain(): UserProfile {
    return UserProfile(
        id = this.id,
        email = this.email,
        username = this.username,
        avatarUrl = this.avatarUrl,
        bio = this.bio,
        age = this.age,
        birthDate = this.birthDate,
        gender = this.gender,
        language = this.language,
        favoriteGenres = this.favoriteGenres.map { it.toDomain() },
        city = this.city?.toDomain(),
        latitude = this.latitude,
        longitude = this.longitude,
        isOnboarded = this.isOnboarded,
        isPublic = this.isPublic
    )
}
