package com.example.hackathon.domain.model

data class TokenPair(
    val accessToken: String,
    val refreshToken: String?
)