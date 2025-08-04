// Этот файл объединяет все use case для GeographyRepository
package com.example.hackathon.domain.usecase

import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.ExchangeLocation
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.repository.GeographyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: GeographyRepository
) {
    operator fun invoke(): Flow<Resource<List<City>>> = repository.getCities()
}

class GetExchangeLocationsUseCase @Inject constructor(
    private val repository: GeographyRepository
) {
    operator fun invoke(): Flow<Resource<List<ExchangeLocation>>> = repository.getExchangeLocations()
}

class GetNearestExchangeLocationUseCase @Inject constructor(
    private val repository: GeographyRepository
) {
    operator fun invoke(): Flow<Resource<ExchangeLocation>> = repository.getNearestExchangeLocation()
}

