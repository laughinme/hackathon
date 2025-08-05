package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksForYouUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(limit: Int? = null): Flow<Resource<List<Book>>> {
        return bookRepository.getBooksForYou(limit)
    }
}
