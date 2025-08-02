package com.example.hackathon.domain.repository

import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Resource<UserProfile>>
    fun updateProfile(request: UserPatchRequest): Flow<Resource<UserProfile>>
    fun updateGenres(genreIds: List<Int>): Flow<Resource<UserProfile>>
}
