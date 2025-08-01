package com.example.hackathon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hackathon.presentation.screens.*
import com.example.hackathon.presentation.screens.onboardingScreens.*
import com.example.hackathon.presentation.screens.tabs.*

/**
 * Главный навигационный компонент приложения.
 * @param navController NavController для управления навигацией.
 * @param startDestination Стартовый маршрут-граф, который определяется в MainActivity.
 */
@Composable
fun AppNavigation(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- ГРАФ 1: АУТЕНТИФИКАЦИЯ ---
        navigation(
            startDestination = Routes.GREETING,
            route = Routes.AUTH_GRAPH
        ) {
            composable(Routes.GREETING) {
                GreetingScreen(
                    onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) },
                    onNavigateToSignIn = { navController.navigate(Routes.SIGN_IN) }
                )
            }
            composable(Routes.SIGN_IN) {
                SignInScreen(
                    onSignInSuccess = {
                        // После входа переходим сразу в главный граф приложения
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) }
                )
            }
            composable(Routes.SIGN_UP) {
                SignUpScreen(
                    onSignUpSuccess = {
                        // После регистрации переходим в граф создания профиля
                        navController.navigate(Routes.PROFILE_CREATION_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onNavigateToSignIn = { navController.navigate(Routes.SIGN_IN) }
                )
            }
        }

        // --- ГРАФ 2: СОЗДАНИЕ ПРОФИЛЯ ---
        navigation(
            startDestination = Routes.AGE_PICKER,
            route = Routes.PROFILE_CREATION_GRAPH
        ) {
            composable(Routes.AGE_PICKER) {
                AgePickerScreen(
                    onNext = { navController.navigate(Routes.GENDER_PICKER) }
                )
            }
            composable(Routes.GENDER_PICKER) {
                GenderPickerScreen(
                    onNext = { navController.navigate(Routes.CITY_PICKER) }
                )
            }
            composable(Routes.CITY_PICKER) {
                CityScreen(
                    onNext = { navController.navigate(Routes.GENRE_PICKER) }
                )
            }
            composable(Routes.GENRE_PICKER) {
                GenresPickerScreen(
                    onProfileComplete = {
                        // Завершили создание профиля, переходим в главный граф
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.PROFILE_CREATION_GRAPH) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        // --- ГРАФ 3: ОСНОВНОЕ ПРИЛОЖЕНИЕ ---
        navigation(
            startDestination = Routes.HOME, // <-- Стартовый маршрут внутри графа
            route = Routes.MAIN_GRAPH
        ) {
            composable(Routes.HOME) {
                // Теперь этот маршрут ведет на наш новый главный экран с Bottom Bar
                MainScreen()
            }
        }
    }
}
