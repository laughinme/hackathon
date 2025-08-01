package com.example.hackathon.domain.model

import java.time.LocalDate

data class UserUpdate(
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val birthDate: LocalDate?,
    val gender: Gender?,
    val cityId: Int?
)