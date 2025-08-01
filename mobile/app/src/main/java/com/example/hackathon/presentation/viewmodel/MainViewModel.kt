package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hackathon.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    // Здесь можно внедрить ваши репозитории для проверки статуса
    // private val userRepository: UserRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow(Routes.AUTH_GRAPH)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        // В реальном приложении эти вызовы будут асинхронными
        val userIsLoggedIn = checkUserLoginStatus()
        val profileIsComplete = checkProfileStatus()

        _startDestination.value = when {
            userIsLoggedIn && profileIsComplete -> Routes.MAIN_GRAPH
            userIsLoggedIn && !profileIsComplete -> Routes.PROFILE_CREATION_GRAPH
            else -> Routes.AUTH_GRAPH
        }
    }

    private fun checkUserLoginStatus(): Boolean {
        // return userRepository.isLoggedIn()
        return true // Для теста
    }

    private fun checkProfileStatus(): Boolean {
        // return userRepository.hasCompletedProfile()
        return true // Для теста
    }
}
