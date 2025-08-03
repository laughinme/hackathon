package com.example.hackathon.domain.model

data class UserProfile(
    val id: String,
    val email: String,
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val age: Int?,
    val birthDate: String?,
    val gender: String?,
    val language: String?,
    val favoriteGenres: List<Genre>,
    val city: City?,
    val latitude: Double?,
    val longitude: Double?,
    val isOnboarded: Boolean,
    val isPublic: Boolean
)