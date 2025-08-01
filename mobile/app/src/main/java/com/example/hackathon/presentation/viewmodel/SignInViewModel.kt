package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    // private val authRepository: AuthRepository
) : ViewModel() {

    // Здесь будет ваша логика: состояние полей, ошибки, загрузка
    // val state = ...

    fun onSignInClicked() {
        // viewModelScope.launch { authRepository.signIn(...) }
        println("Sign In button clicked in ViewModel!")
    }
}