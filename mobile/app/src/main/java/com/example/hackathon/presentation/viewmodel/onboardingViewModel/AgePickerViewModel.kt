package com.example.hackathon.presentation.viewmodel.onboardingViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

const val AGE_PICKER_VIEWMODEL_TAG = "AgePickerViewModel"

class AgePickerViewModel : ViewModel() {

    private val _pickedDate = MutableStateFlow("")

    fun onPickedDate(date: LocalDate) {
        _pickedDate.value = date.toString()
    }

    fun onAgePickerClicked() {
        Log.d(AGE_PICKER_VIEWMODEL_TAG, "Выбрана дата рождения: $_pickedDate")
    }
}