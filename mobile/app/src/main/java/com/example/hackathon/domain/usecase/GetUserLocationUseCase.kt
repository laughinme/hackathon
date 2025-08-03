package com.example.hackathon.domain.usecase

import android.location.Location
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.location.LocationTracker
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationTracker: LocationTracker
) {
    suspend operator fun invoke(): Resource<Location> {
        return locationTracker.getCurrentLocation()
    }
}