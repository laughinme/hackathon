package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.data.remote.dto.UserLoginRequest
import com.example.hackathon.data.remote.dto.UserRegisterRequest
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.TokenPair
import com.example.hackathon.domain.usecase.LoginUseCase
import com.example.hackathon.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val AUTH_VIEWMODEL_TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _authState = MutableStateFlow<Resource<TokenPair>?>(null)
    val authState = _authState.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onSignInClicked() {
        viewModelScope.launch {
            loginUseCase(UserLoginRequest(email.value, password.value))
                .collect { result ->
                    _authState.value = result
                }
        }
    }

    fun onSignUpClicked() {
        viewModelScope.launch {
            val username = "some_username" // Возьми из соответствующего StateFlow
            registerUseCase(UserRegisterRequest(email.value, password.value, username))
                .collect { result ->
                    _authState.value = result
                }
        }
    }
}