package com.example.hackathon.domain.repository

import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.model.UserProfileUpdate
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    fun getProfile(): Flow<Resource<UserProfile>>
    fun updateFullProfile(
        profileData: UserProfileUpdate,
    ): Flow<Resource<UserProfile>>
    fun updateProfilePicture(file: File): Flow<Resource<UserProfile>>
}
