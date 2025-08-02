package com.example.hackathon.domain.model

import com.example.hackathon.data.remote.dto.UserModelDto

data class UserProfile(
    val email: String,
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val birthDate: String?,
    val gender: String?,
    val cityId: Int?,
    val isOnboarded: Boolean
)

// Маппер для преобразования DTO в доменную модель
fun UserModelDto.toDomain(): UserProfile {
    return UserProfile(
        email = this.email,
        username = this.username,
        avatarUrl = this.avatarUrl,
        bio = this.bio,
        birthDate = this.birthDate,
        gender = this.gender,
        cityId = this.cityId,
        isOnboarded = this.isOnboarded
    )
}