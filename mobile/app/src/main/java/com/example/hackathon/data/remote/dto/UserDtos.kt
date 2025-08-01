package com.example.hackathon.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Модель пользователя, получаемая от API.
 * Соответствует схеме UserModel в Swagger.
 */
data class UserModelDto(
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
    @SerializedName("gender")
    val gender: String?, // "male", "female", "unknown"
    @SerializedName("city_id")
    val cityId: Int?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("is_onboarded")
    val isOnboarded: Boolean,
    @SerializedName("banned")
    val isBanned: Boolean
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
    val birthDate: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("city_id")
    val cityId: Int? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null
)

/**
 * Тело запроса для обновления любимых жанров.
 * Соответствует схеме GenresPatch в Swagger.
 */
data class GenresPatchRequest(
    @SerializedName("favorite_genres")
    val favoriteGenres: List<Int>
)

