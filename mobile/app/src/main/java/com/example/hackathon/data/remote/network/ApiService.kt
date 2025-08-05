package com.example.hackathon.data.remote.network


import com.example.hackathon.data.remote.dto.BookCreateRequest
import com.example.hackathon.data.remote.dto.BookModelDto
import com.example.hackathon.data.remote.dto.CityModelDto
import com.example.hackathon.data.remote.dto.ExchangeCreateRequest
import com.example.hackathon.data.remote.dto.ExchangeLocationDto
import com.example.hackathon.data.remote.dto.ExchangeModelDto
import com.example.hackathon.data.remote.dto.GenreModelDto
import com.example.hackathon.data.remote.dto.GenresPatchRequest
import com.example.hackathon.data.remote.dto.TokenPairDto
import com.example.hackathon.data.remote.dto.UserLoginRequest
import com.example.hackathon.data.remote.dto.UserModelDto
import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.data.remote.dto.UserRegisterRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
     * Получение информации о текущем пользователе.
     * Для этого эндпоинта требуется токен доступа.
     */
    @GET("/api/v1/users/me/")
    suspend fun getMe(): Response<UserModelDto>

    /**
     * Частично обновляет информацию о пользователе.
     * @param request Тело запроса с полями для обновления.
     */
    @PATCH("/api/v1/users/me/")
    suspend fun updateProfile(@Body request: UserPatchRequest): Response<UserModelDto>

    /**
     * Полностью заменяет список любимых жанров пользователя.
     * @param request Тело запроса со списком ID жанров.
     */
    @PUT("/api/v1/users/me/genres")
    suspend fun updateGenres(@Body request: GenresPatchRequest): Response<UserModelDto>

    /**
     * Загружает файл изображения в качестве аватара пользователя.
     * @param file Файл изображения в формате MultipartBody.Part.
     * @return Обновленная модель пользователя с новым URL аватара.
     */
    @Multipart
    @PUT("/api/v1/users/me/picture")
    suspend fun updateProfilePicture(
        @Part file: MultipartBody.Part
    ): Response<UserModelDto>

    /**
     * Создание новой книги.
     * Требуется токен доступа.
     * @param request Тело запроса с данными о книге.
     * @return Модель созданной книги.
     */
    @POST("/api/v1/books/create")
    suspend fun createBook(@Body request: BookCreateRequest): Response<BookModelDto>

    /**
     * Загрузка фотографий для книги.
     * Требуется токен доступа.
     * @param bookId ID книги, для которой загружаются фото.
     * @param files Список файлов (фотографий).
     * @return Модель книги с обновленными URL фотографий.
     */
    @Multipart
    @PUT("/api/v1/books/{book_id}/photos")
    suspend fun uploadBookPhotos(
        @Path("book_id") bookId: String,
        @Part files: List<MultipartBody.Part>
    ): Response<BookModelDto>

    /**
     * Получение списка всех доступных жанров.
     * @return Список моделей жанров.
     */
    @GET("/api/v1/books/genres/")
    suspend fun getGenres(): Response<List<GenreModelDto>>

    /**
     * Получение списка всех поддерживаемых городов.
     * @return Список моделей городов.
     */
    @GET("/api/v1/geo/cities/")
    suspend fun listCities(): Response<List<CityModelDto>>

    /**
     * Получение списка всех точек обмена, отсортированных по расстоянию до пользователя.
     * Требуется авторизация (HTTPBearer).
     * @param limit Максимальное количество возвращаемых точек (по умолчанию 30).
     * @param filter Нужно ли сортировать точки по расстоянию от пользователя (по умолчанию true).
     * @return Список моделей точек обмена.
     */
    @GET("/api/v1/geo/exchange_locations/")
    suspend fun listLocations(
        @Query("limit") limit: Int? = null,
        @Query("filter") filter: Boolean? = null
    ): Response<List<ExchangeLocationDto>>

    /**
     * Получение ближайшей точки обмена к пользователю.
     * Требуется авторизация (HTTPBearer).
     * @return Модель ближайшей точки обмена.
     */
    @GET("/api/v1/geo/exchange_locations/nearest")
    suspend fun getNearestExchangePoint(): Response<ExchangeLocationDto>

    /**
     * Получение списка книг "для вас".
     * Требуется токен доступа.
     * @param limit Ограничение количества книг.
     * @return Список моделей книг.
     */
    @GET("/api/v1/books/for_you")
    suspend fun getBooksForYou(@Query("limit") limit: Int? = null): Response<List<BookModelDto>>

    /**
     * Записывает клик пользователя по книге.
     * Используется для системы рекомендаций.
     * @param bookId ID книги, по которой кликнули.
     */
    @POST("/api/v1/books/{book_id}/click")
    suspend fun recordClick(@Path("book_id") bookId: String): Response<Unit>

    /**
     * Записывает лайк пользователя для книги.
     * @param bookId ID книги, которую лайкнули.
     */
    @POST("/api/v1/books/{book_id}/like")
    suspend fun likeBook(@Path("book_id") bookId: String): Response<Unit>

    /**
     * Резервирует книгу, инициируя процесс обмена.
     * @param bookId ID книги для резерва.
     * @param request Тело запроса с дополнительной информацией (комментарий, время встречи).
     * @return Модель созданного обмена.
     */
    @POST("/api/v1/books/{book_id}/reserve")
    suspend fun reserveBook(
        @Path("book_id") bookId: String,
        @Body request: ExchangeCreateRequest
    ): Response<ExchangeModelDto>
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
