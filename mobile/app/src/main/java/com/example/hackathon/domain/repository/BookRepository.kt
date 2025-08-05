package com.example.hackathon.domain.repository

import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.Exchange
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.ReserveBookParams
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

    /**
     * Получает список книг "для вас".
     * @param limit Ограничение на количество книг.
     */
    fun getBooksForYou(limit: Int? = null): Flow<Resource<List<Book>>>

    /**
     * Записывает клик пользователя по книге.
     * @param bookId ID книги.
     */
    fun recordClick(bookId: String): Flow<Resource<Unit>>

    /**
     * Отправляет "лайк" для книги.
     * @param bookId ID книги.
     */
    fun likeBook(bookId: String): Flow<Resource<Unit>>

    /**
     * Резервирует книгу, инициируя процесс обмена.
     * @param params Параметры для резервирования.
     */
    fun reserveBook(params: ReserveBookParams): Flow<Resource<Exchange>>
}