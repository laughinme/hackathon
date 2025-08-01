package com.example.hackathon.domain.model

import java.time.LocalDate

data class User(
    val email: String,
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val birthDate: LocalDate?,
    val gender: Gender?,
    val cityId: Int?,
    val isOnboarded: Boolean,
    val isBanned: Boolean
)