package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UpdateProfilePictureUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(file: File): Flow<Resource<UserProfile>> {
        return userRepository.updateProfilePicture(file)
    }
}