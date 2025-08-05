package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.model.ReserveBookParams
import com.example.hackathon.domain.repository.BookRepository
import javax.inject.Inject

class ReserveBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(params: ReserveBookParams) = bookRepository.reserveBook(params)
}
