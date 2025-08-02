package com.example.hackathon.data.repository.impl

import com.example.hackathon.data.remote.dto.GenresPatchRequest
import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.data.remote.network.ApiService
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.model.toDomain
import com.example.hackathon.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {

    override fun getProfile(): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getMe()
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.toDomain()))
            } else {
                emit(Resource.Error(response.message() ?: "Не удалось получить профиль"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Ошибка сети"))
        } catch (e: IOException) {
            emit(Resource.Error("Не удалось подключиться к серверу."))
        }
    }

    override fun updateProfile(request: UserPatchRequest): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.updateProfile(request)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.toDomain()))
            } else {
                emit(Resource.Error(response.message() ?: "Не удалось обновить профиль"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Ошибка сети"))
        } catch (e: IOException) {
            emit(Resource.Error("Не удалось подключиться к серверу."))
        }
    }

    override fun updateGenres(genreIds: List<Int>): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading())
        try {
            val request = GenresPatchRequest(favoriteGenres = genreIds)
            val response = apiService.updateGenres(request)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.toDomain()))
            } else {
                emit(Resource.Error(response.message() ?: "Не удалось обновить жанры"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Ошибка сети"))
        } catch (e: IOException) {
            emit(Resource.Error("Не удалось подключиться к серверу."))
        }
    }
}