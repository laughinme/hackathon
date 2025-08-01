package com.example.hackathon.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hackathon.presentation.navigation.BottomNavItem
import com.example.hackathon.presentation.screens.tabs.ChatTabScreen
import com.example.hackathon.presentation.screens.tabs.FriendsTabScreen
import com.example.hackathon.presentation.screens.tabs.HomeTabScreen
import com.example.hackathon.presentation.screens.tabs.ProfileTabScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val bottomBarNavController = rememberNavController()

    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                BottomNavGraph(navController = bottomBarNavController)
            }
        },
        bottomBar = {
            AppBottomAppBar(navController = bottomBarNavController)
        },
        floatingActionButton = {
            FloatingActionButton(
                // КЛЮЧЕВОЕ ИЗМЕНЕНИЕ: Добавляем сдвиг по вертикали, чтобы
                // FAB "опустилась" в вырез на панели.
                modifier = Modifier.offset(y = 60.dp),
                onClick = { /* TODO: Действие для центральной кнопки */ },
                shape = FloatingActionButtonDefaults.shape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    )
}

/**
 * ИСПРАВЛЕННАЯ НИЖНЯЯ ПАНЕЛЬ НАВИГАЦИИ (BottomAppBar).
 *
 * В Material 3 `Scaffold` автоматически создает пространство для FAB при
 * использовании `FabPosition.Center`. Наша задача - лишь правильно
 * расположить элементы внутри `BottomAppBar`, оставив пустое место
 * в центре с помощью `Spacer`.
 */
@Composable
private fun AppBottomAppBar(navController: NavHostController) {
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Friends,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )
    val leftItems = navItems.subList(0, navItems.size / 2)
    val rightItems = navItems.subList(navItems.size / 2, navItems.size)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Левая группа иконок
        leftItems.forEach { screen ->
            BottomAppBarItem(
                modifier = Modifier.weight(1f),
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }

        // Невидимый Spacer в центре, который создает место для FAB.
        Spacer(Modifier.weight(1f))

        // Правая группа иконок
        rightItems.forEach { screen ->
            BottomAppBarItem(
                modifier = Modifier.weight(1f),
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

/**
 * Элемент навигации для BottomAppBar.
 */
@Composable
private fun RowScope.BottomAppBarItem(
    modifier: Modifier = Modifier,
    screen: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    IconButton(
        modifier = modifier,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }) {
        Icon(
            imageVector = screen.icon,
            contentDescription = screen.title,
            modifier = Modifier.size(28.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


/**
 * Навигационный граф для вкладок BottomAppBar.
 */
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(route = BottomNavItem.Home.route) { HomeTabScreen() }
        composable(route = BottomNavItem.Friends.route) { FriendsTabScreen() }
        composable(route = BottomNavItem.Chat.route) { ChatTabScreen() }
        composable(route = BottomNavItem.Profile.route) { ProfileTabScreen() }
    }
}