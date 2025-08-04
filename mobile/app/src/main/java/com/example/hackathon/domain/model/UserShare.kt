package com.example.hackathon.domain.model

/**
 * Упрощенная модель пользователя для публичного отображения.
 */
data class UserShare(
    val id: String,
    val username: String?,
    val avatarUrl: String?,
    val city: City
)
