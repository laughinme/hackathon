package com.example.hackathon.domain.model

import java.io.File

data class CreateBookParams(
    val title: String,
    val description: String?,
    val extraTerms: String?,
    val authorId: Int,
    val genreId: Int,
    val language: String,
    val pages: Int?,
    val condition: BookCondition,
    val exchangeLocationId: Int,
    val photos: List<File> // Список файлов для загрузки
)