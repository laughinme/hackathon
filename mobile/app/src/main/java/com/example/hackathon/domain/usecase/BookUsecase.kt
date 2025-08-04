package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(params: CreateBookParams): Flow<Resource<Book>> {
        return bookRepository.createBook(params)
    }
}

class GetGenresUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Flow<Resource<List<Genre>>> {
        return bookRepository.getGenres()
    }
}