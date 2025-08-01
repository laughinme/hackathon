package com.example.hackathon.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

const val ONBOARDING_VIEWMODEL_TAG = "OnboardingViewModel"
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    //Тут UseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onSignInClicked() {
        // viewModelScope.launch { authRepository.signIn(...) }
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Sign In button clicked in ViewModel!")
    }

    fun onSignUpClicked() {
        // viewModelScope.launch { authRepository.signIn(...) }
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Sign In button clicked in ViewModel!")
    }

    private val _pickedDate = MutableStateFlow("")
    val pickeDate = _pickedDate.asStateFlow()

    fun onPickedDate(date: LocalDate) {
        _pickedDate.value = date.toString()
    }

    fun onAgePickerClicked() {
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Выбрана дата рождения: $_pickedDate")
    }

}