package com.example.hackathon.data.repository.impl

import com.example.hackathon.data.remote.dto.toDomain
import com.example.hackathon.data.remote.network.ApiService
import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.ExchangeLocation
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.repository.GeographyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeographyRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GeographyRepository {
    override fun getCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.listCities()
            if (response.isSuccessful) {
                val cities = response.body()?.map { it.toDomain() } ?: emptyList()
                emit(Resource.Success(cities))
            } else {
                emit(Resource.Error("Ошибка загрузки городов: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Неизвестная ошибка"))
        }
    }

    override fun getExchangeLocations(): Flow<Resource<List<ExchangeLocation>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.listLocations()
            if (response.isSuccessful) {
                val locations = response.body()?.map { it.toDomain() } ?: emptyList()
                emit(Resource.Success(locations))
            } else {
                emit(Resource.Error("Ошибка загрузки точек обмена: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Неизвестная ошибка"))
        }
    }

    override fun getNearestExchangeLocation(): Flow<Resource<ExchangeLocation>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getNearestExchangePoint()
            if (response.isSuccessful) {
                val location = response.body()?.toDomain()
                if (location != null) {
                    emit(Resource.Success(location))
                } else {
                    emit(Resource.Error("Пустой ответ от сервера"))
                }
            } else {
                emit(Resource.Error("Ошибка загрузки ближайшей точки: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Неизвестная ошибка"))
        }
    }
}

