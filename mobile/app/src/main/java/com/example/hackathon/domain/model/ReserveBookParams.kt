package com.example.hackathon.domain.model

import java.time.OffsetDateTime

/**
 * Параметры, необходимые для создания запроса на резервирование книги.
 */
data class ReserveBookParams(
    val bookId: String,
    val meetingTime: OffsetDateTime?,
    val comment: String?
)