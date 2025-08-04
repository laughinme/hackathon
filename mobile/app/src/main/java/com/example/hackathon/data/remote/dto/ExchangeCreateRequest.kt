package com.example.hackathon.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

/**
 * DTO для запроса на создание обмена (резервирование книги).
 * @param meetingTime Предполагаемое время встречи.
 * @param comment Комментарий от запрашивающего к владельцу книги.
 */
data class ExchangeCreateRequest(
    @SerializedName("meeting_time")
    val meetingTime: OffsetDateTime?,
    @SerializedName("comment")
    val comment: String?
)
