package com.example.hackathon.domain.model

/**
 * Стадии процесса обмена книгами.
 */
enum class ExchangeProgress {
    CREATED,
    ACCEPTED,
    DECLINED,
    FINISHED,
    CANCELED,
    UNKNOWN // Для случаев, когда с бэкенда приходит неизвестное значение
}
