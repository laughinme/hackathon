package com.example.hackathon.domain.model

import java.time.OffsetDateTime

/**
 * Представление обмена книгами в domain слое.
 */
data class Exchange(
    val id: String,
    val owner: UserShare,
    val requester: UserShare,
    val book: Book,
    val progress: ExchangeProgress,
    val cancelReason: String?,
    val comment: String?,
    val meetingTime: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?
)