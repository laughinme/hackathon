package com.example.hackathon.domain.usecase

import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    operator fun invoke() = repository.getProfile()
}

class UpdateProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    operator fun invoke(request: UserPatchRequest) = repository.updateProfile(request)
}

class UpdateGenresUseCase @Inject constructor(private val repository: ProfileRepository) {
    operator fun invoke(genreIds: List<Int>) = repository.updateGenres(genreIds)
}