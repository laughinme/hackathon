package com.example.hackathon.domain.repository

import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория, определяющий контракт для работы с данными книг.
 * Теперь использует Flow<Resource<T>> для асинхронной передачи состояний (Loading, Success, Error).
 */
interface BookRepository {

    /**
     * Создает книгу на сервере и загружает для нее фотографии.
     */
    fun createBook(params: CreateBookParams): Flow<Resource<Book>>

    /**
     * Получает список всех доступных жанров.
     */
    fun getGenres(): Flow<Resource<List<Genre>>>
}