package com.example.hackathon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.hackathon.presentation.navigation.AppBottomAppBar
import com.example.hackathon.presentation.navigation.AppNavigation
import com.example.hackathon.presentation.navigation.Routes
import com.example.hackathon.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val startDestination by viewModel.startDestination.collectAsState()

                // Список маршрутов, на которых должен отображаться BottomAppBar
                val bottomBarScreens = listOf(
                    Routes.HOME_TAB,
                    Routes.FRIENDS_TAB,
                    Routes.CHAT_TAB,
                    Routes.PROFILE_TAB
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val shouldShowBottomBar = currentRoute in bottomBarScreens

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            AppBottomAppBar(navController = navController)
                        }
                    },
                    floatingActionButton = {
                        if (shouldShowBottomBar) {
                            FloatingActionButton(
                                modifier = Modifier.offset(y = 60.dp),
                                onClick = { navController.navigate(Routes.ADD_BOOK) },
                                shape = FloatingActionButtonDefaults.shape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Добавить",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppNavigation(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}
