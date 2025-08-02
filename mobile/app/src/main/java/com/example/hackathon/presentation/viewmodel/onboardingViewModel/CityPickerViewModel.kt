package com.example.hackathon.presentation.viewmodel.onboardingViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

const val CITY_PICKER_VIEWMODEL_TAG = "CityPickerViewModel"

class CityPickerViewModel : ViewModel() {

    private val _expanded = MutableStateFlow(false)
    val expanded = _expanded.asStateFlow()

    private val _selectedCity = MutableStateFlow("")
    val selectedCity = _selectedCity.asStateFlow()

    fun onExpandedChanged() {
        _expanded.value = !_expanded.value
    }

    fun onCitySelected(city: String) {
        _selectedCity.value = city
    }

    fun onCityClicked() {
        Log.d(CITY_PICKER_VIEWMODEL_TAG, "Выбран город: $_selectedCity")
    }
}