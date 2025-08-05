package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.repository.BookRepository
import jakarta.inject.Inject

class RecordClickUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(bookId: String) = bookRepository.recordClick(bookId)
}