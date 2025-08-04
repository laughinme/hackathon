package com.example.hackathon.data.repository.impl

import android.content.Context
import androidx.core.net.toUri
import com.example.hackathon.data.remote.dto.toDomain
import com.example.hackathon.data.remote.dto.toRequest
import com.example.hackathon.data.remote.network.ApiService
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.repository.BookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context // Инжектим контекст для доступа к ContentResolver
) : BookRepository {

    override fun createBook(params: CreateBookParams): Flow<Resource<Book>> = flow {
        emit(Resource.Loading())
        try {
            // Шаг 1: Создаем книгу
            val createBookRequest = params.toRequest()
            val createResponse = apiService.createBook(createBookRequest)

            if (!createResponse.isSuccessful) {
                emit(Resource.Error("Failed to create book. Code: ${createResponse.code()}"))
                return@flow
            }

            val createdBookDto = createResponse.body()
                ?: run {
                    emit(Resource.Error("Empty response body after creating book"))
                    return@flow
                }

            // Шаг 2: Если есть фото, загружаем их
            val finalBookDto = if (params.photos.isNotEmpty()) {
                val photoParts = params.photos.map { file ->
                    // Получаем MIME-тип файла
                    val mimeType = context.contentResolver.getType(file.toUri()) ?: "image/jpeg"
                    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("files", file.name, requestFile)
                }

                val uploadResponse = apiService.uploadBookPhotos(createdBookDto.id, photoParts)

                if (!uploadResponse.isSuccessful) {
                    emit(Resource.Error("Failed to upload photos. Code: ${uploadResponse.code()}"))
                    return@flow
                }
                uploadResponse.body() ?: run {
                    emit(Resource.Error("Empty response body after uploading photos"))
                    return@flow
                }
            } else {
                createdBookDto
            }

            emit(Resource.Success(finalBookDto.toDomain()))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override fun getGenres(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getGenres()
            if (response.isSuccessful) {
                val genresDto = response.body() ?: emptyList()
                emit(Resource.Success(genresDto.map { it.toDomain() }))
            } else {
                emit(Resource.Error("Failed to fetch genres. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override fun getBooksForYou(limit: Int?): Flow<Resource<List<Book>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getBooksForYou(limit = limit)
            if (response.isSuccessful) {
                val booksDto = response.body() ?: emptyList()
                emit(Resource.Success(booksDto.map { it.toDomain() }))
            } else {
                emit(Resource.Error("Failed to fetch books for you. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
}