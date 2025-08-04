package com.example.hackathon.domain.model

import java.time.LocalDate

data class UserProfileUpdate(
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val birthDate: LocalDate?,
    val gender: String?,
    val language: String?,
    val cityId: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val isPublic: Boolean?,
    val favoriteGenreIds: List<Int>
)