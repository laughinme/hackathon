package com.example.hackathon.presentation.viewmodel.onboardingViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

const val GENDER_PICKER_VIEWMODEL_TAG = "GenderPickerViewModel"

class GenderPickerViewModel : ViewModel() {

    private val _selectedGender = MutableStateFlow("")
    val selectedGender = _selectedGender.asStateFlow()

    fun onSelectedGenderChanged(newGender: String) {
        _selectedGender.value = newGender
    }

    fun onGenderPickerClicked() {
        Log.d(GENDER_PICKER_VIEWMODEL_TAG, "Выбран гендер: $_selectedGender")
    }
}