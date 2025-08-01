package com.example.hackathon.data.remote.network


import com.example.hackathon.data.remote.dto.TokenPairDto
import com.example.hackathon.data.remote.dto.UserLoginRequest
import com.example.hackathon.data.remote.dto.UserModelDto
import com.example.hackathon.data.remote.dto.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    /**
     * Регистрация нового пользователя.
     * @param clientHeader Заголовок, указывающий тип клиента ("mobile").
     * @param request Тело запроса с данными для регистрации.
     */
    @POST("/api/v1/auth/register")
    suspend fun register(
        @Header("X-Client") clientHeader: String = "mobile",
        @Body request: UserRegisterRequest
    ): Response<TokenPairDto>

    /**
     * Аутентификация пользователя.
     * @param clientHeader Заголовок, указывающий тип клиента ("mobile").
     * @param request Тело запроса с учетными данными.
     */
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Header("X-Client") clientHeader: String = "mobile",
        @Body request: UserLoginRequest
    ): Response<TokenPairDto>

    /**
     * Выход пользователя из системы.
     * Для этого эндпоинта требуется токен доступа в заголовке Authorization.
     */
    @POST("/api/v1/auth/logout")
    suspend fun logout(): Response<Unit> // Ответ 200 без тела

    /**
     * Обновление пары токенов с использованием refresh token.
     * ВАЖНО: Этот запрос выполняется особым образом через Authenticator.
     * Мы создадим отдельный интерфейс для этого, чтобы избежать рекурсивных вызовов в interceptor.
     */
    @POST("/api/v1/auth/refresh")
    suspend fun refreshTokens(): Response<TokenPairDto>

    /**
     * Получение информации о текущем пользователе.
     * Для этого эндпоинта требуется токен доступа.
     */
    @GET("/api/v1/users/me/")
    suspend fun getMe(): Response<  UserModelDto>
}

/**
 * Отдельный интерфейс только для эндпоинта обновления токенов.
 * Это помогает избежать проблем, когда AuthInterceptor пытается добавить
 * заголовок Authorization к запросу, который сам обновляет токен.
 */
interface TokenRefreshApiService {
    @POST("/api/v1/auth/refresh")
    suspend fun refreshTokens(@Header("Authorization") refreshToken: String): Response<TokenPairDto>
}
