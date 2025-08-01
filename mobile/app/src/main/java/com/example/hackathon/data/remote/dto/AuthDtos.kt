package com.example.hackathon.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Тело запроса для входа пользователя.
 * Соответствует схеме UserLogin в Swagger.
 */
data class UserLoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

/**
 * Тело запроса для регистрации пользователя.
 * Соответствует схеме UserRegister в Swagger.
 */
data class UserRegisterRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String? = null
)

/**
 * Ответ, содержащий пару токенов доступа и обновления.
 * Соответствует схеме TokenPair в Swagger.
 */
data class TokenPairDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String? // Может быть null для web-клиента
)