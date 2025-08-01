package com.example.hackathon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.hackathon.presentation.navigation.AppNavigation
import com.example.hackathon.presentation.navigation.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // --- ЛОГИКА ОПРЕДЕЛЕНИЯ СТАРТОВОГО ЭКРАНА ---
        // В реальном приложении эта логика будет в ViewModel.
        val userIsLoggedIn = checkUserLoginStatus()
        val profileIsComplete = checkProfileStatus() // <-- Новая проверка

        val startDestination = when {
            // Если пользователь вошел и профиль заполнен -> главный экран
            userIsLoggedIn && profileIsComplete -> Routes.MAIN_GRAPH
            // Если вошел, но профиль не заполнен -> экран создания профиля
            userIsLoggedIn && !profileIsComplete -> Routes.PROFILE_CREATION_GRAPH
            // Если не вошел -> экран аутентификации
            else -> Routes.AUTH_GRAPH
        }
        // ------------------------------------------------

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }

    /**
     * Функция-заглушка для проверки статуса пользователя.
     * Замените ее на свою реальную логику.
     */
    private fun checkUserLoginStatus(): Boolean {
        // Например: return dataStore.getToken().isNotBlank()
        return false
    }

    /**
     * Функция-заглушка для проверки, заполнен ли профиль.
     * Замените ее на свою реальную логику.
     */
    private fun checkProfileStatus(): Boolean {
        // Например: return userRepository.hasCompletedProfile()
        return false
    }
}
