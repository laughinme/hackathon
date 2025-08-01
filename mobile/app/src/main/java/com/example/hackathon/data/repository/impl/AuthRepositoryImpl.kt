package com.example.hackathon.data.repository.impl

import com.example.hackathon.data.remote.dto.TokenPairDto
import com.example.hackathon.data.remote.dto.UserLoginRequest
import com.example.hackathon.data.remote.dto.UserRegisterRequest
import com.example.hackathon.data.remote.network.ApiService
import com.example.hackathon.data.remote.network.TokenManager
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.TokenPair
import com.example.hackathon.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun login(request: UserLoginRequest): Flow<Resource<TokenPair>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(request = request)
            if (response.isSuccessful && response.body() != null) {
                val tokenDto = response.body()!!
                tokenManager.saveTokens(tokenDto.accessToken, tokenDto.refreshToken)
                emit(Resource.Success(tokenDto.toDomain()))
            } else {
                // Например, код 401 - неверные учетные данные
                val errorMsg = response.errorBody()?.string() ?: "Ошибка входа"
                emit(Resource.Error(errorMsg))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Ошибка сети"))
        } catch (e: IOException) {
            emit(Resource.Error("Не удалось подключиться к серверу. Проверьте интернет-соединение."))
        }
    }

    override fun register(request: UserRegisterRequest): Flow<Resource<TokenPair>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.register(request = request)
            if (response.isSuccessful && response.body() != null) {
                val tokenDto = response.body()!!
                tokenManager.saveTokens(tokenDto.accessToken, tokenDto.refreshToken)
                emit(Resource.Success(tokenDto.toDomain()))
            } else {
                // Например, код 409 - пользователь уже существует
                val errorMsg = response.errorBody()?.string() ?: "Ошибка регистрации"
                emit(Resource.Error(errorMsg))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Ошибка сети"))
        } catch (e: IOException) {
            emit(Resource.Error("Не удалось подключиться к серверу. Проверьте интернет-соединение."))
        }
    }

    override fun logout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                tokenManager.clearTokens()
                emit(Resource.Success(Unit))
            } else {
                // Если токен уже невалиден, все равно чистим его локально
                tokenManager.clearTokens()
                val errorMsg = response.errorBody()?.string() ?: "Ошибка выхода"
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            tokenManager.clearTokens()
            emit(Resource.Error(e.localizedMessage ?: "Произошла ошибка"))
        }
    }
}

// Простая функция-маппер для преобразования DTO в доменную модель
private fun TokenPairDto.toDomain(): TokenPair {
    return TokenPair(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}