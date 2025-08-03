package com.example.hackathon.data.repository.impl

import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.example.hackathon.data.remote.dto.GenresPatchRequest
import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.data.remote.dto.toDomain
import com.example.hackathon.data.remote.network.ApiService
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.model.UserProfileUpdate
import com.example.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

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

    override fun updateFullProfile(
        profileData: UserProfileUpdate
    ): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading())
        try {
            // --- ШАГ 1: Обновляем основные данные профиля ---
            // Репозиторий теперь просто передает ID, которые ему дала ViewModel
            val profileRequest = UserPatchRequest(
                username = profileData.username,
                avatarUrl = profileData.avatarUrl,
                bio = profileData.bio,
                birthDate = profileData.birthDate?.format(DateTimeFormatter.ISO_LOCAL_DATE),
                gender = profileData.gender,
                language = profileData.language,
                cityId = profileData.cityId, // <-- Просто используем переданный ID
                latitude = profileData.latitude,
                longitude = profileData.longitude,
                isPublic = profileData.isPublic
            )

            val profileResponse = apiService.updateProfile(profileRequest)
            if (!profileResponse.isSuccessful) {
                emit(Resource.Error("Failed to update profile. Code: ${profileResponse.code()}"))
                return@flow
            }

            // --- ШАГ 2: Обновляем жанры в отдельном запросе ---
            val genresRequest = GenresPatchRequest(favoriteGenres = profileData.favoriteGenreIds) // <-- Просто используем переданный список ID

            val genresResponse = apiService.updateGenres(genresRequest)
            if (!genresResponse.isSuccessful) {
                emit(Resource.Error("Profile updated, but failed to update genres. Code: ${genresResponse.code()}"))
                return@flow
            }

            // --- УСПЕХ: Оба запроса прошли ---
            val finalUserProfileDto = genresResponse.body()!!
            emit(Resource.Success(finalUserProfileDto.toDomain()))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

        override fun updateProfilePicture(file: File): Flow<Resource<UserProfile>> = flow {
            emit(Resource.Loading())
            try {
                // Создаем RequestBody из файла
                val extension = file.extension

                // 2. Получаем MIME-тип из расширения. Это надежнее.
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "image/jpeg"

                // 3. Создаем RequestBody
                val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val response = apiService.updateProfilePicture(body)
                if (response.isSuccessful) {
                    val updatedProfile = response.body()?.toDomain()
                    if (updatedProfile != null) {
                        emit(Resource.Success(updatedProfile))
                    } else {
                        emit(Resource.Error("Empty response after picture upload"))
                    }
                } else {
                    emit(Resource.Error("Failed to upload picture. Code: ${response.code()}"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "An unknown error occurred while uploading picture"))
            }
        }
}