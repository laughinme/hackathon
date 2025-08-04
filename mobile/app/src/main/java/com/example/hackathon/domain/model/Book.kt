package com.example.hackathon.domain.model

import java.time.OffsetDateTime

data class Book(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String?,
    val extraTerms: String?,
    val author: Author,
    val genre: Genre,
    val language: String,
    val pages: Int?,
    val condition: BookCondition,
    val photoUrls: List<String>,
    val exchangeLocation: ExchangeLocation,
    val isAvailable: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?
)