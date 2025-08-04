package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.repository.BookRepository
import javax.inject.Inject

class LikeBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(bookId: String) = bookRepository.likeBook(bookId)
}