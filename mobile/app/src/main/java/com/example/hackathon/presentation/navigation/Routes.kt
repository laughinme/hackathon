package com.example.hackathon.presentation.navigation

/**
 * Объект для хранения всех маршрутов навигации.
 */
object Routes {
    // --- ГРАФЫ НАВИГАЦИИ ---
    const val AUTH_GRAPH = "auth_graph"
    const val PROFILE_CREATION_GRAPH = "profile_creation_graph" // <-- Новый граф
    const val MAIN_GRAPH = "main_graph"


    // --- ЭКРАНЫ ГРАФА АУТЕНТИФИКАЦИИ (AUTH_GRAPH) ---
    const val GREETING = "greeting"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"


    // --- ЭКРАНЫ ГРАФА СОЗДАНИЯ ПРОФИЛЯ (PROFILE_CREATION_GRAPH) ---
    const val AGE_PICKER = "age_picker"
    const val GENDER_PICKER = "gender_picker"
    const val CITY_PICKER = "city_picker"
    const val GENRE_PICKER = "genre_picker"

    // --- ЭКРАНЫ ОСНОВНОГО ГРАФА (MAIN_GRAPH) ---
    const val HOME = "home"
}
