package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.data.remote.network.TokenManager
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.usecase.LogoutUseCase
import com.example.hackathon.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager, // Для проверки статуса входа
) : ViewModel() {

    private val _startDestination = MutableStateFlow(Routes.AUTH_GRAPH)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        viewModelScope.launch {
            // Проверяем наличие токена. first() возьмет первое значение из Flow
            val accessToken = tokenManager.getAccessToken().first()
            val userIsLoggedIn = accessToken != null

            // Логика для проверки профиля (оставим пока как есть)
            val profileIsComplete = checkProfileStatus()

            _startDestination.value = when {
                userIsLoggedIn && profileIsComplete -> Routes.MAIN_GRAPH
                userIsLoggedIn && !profileIsComplete -> Routes.PROFILE_CREATION_GRAPH
                else -> Routes.AUTH_GRAPH
            }
        }
    }

    private fun checkProfileStatus(): Boolean {
        // Здесь будет твоя логика проверки, завершен ли онбординг
        return true // Для теста
    }
}