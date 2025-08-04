package com.example.hackathon.domain.repository

import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.ExchangeLocation
import com.example.hackathon.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface GeographyRepository {

    fun getCities() : Flow<Resource<List<City>>>

    fun getExchangeLocations() : Flow<Resource<List<ExchangeLocation>>>

    fun getNearestExchangeLocation() : Flow<Resource<ExchangeLocation>>
}