package com.example.hackathon.presentation.screens.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

// Stateful-контейнер
@Composable
fun ProfileTabScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val username by viewModel.username.collectAsStateWithLifecycle()
    val bio by viewModel.bio.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Показываем Snackbar при ошибке или успешном сохранении
    LaunchedEffect(key1 = profileState) {
        val state = profileState
        if (state is Resource.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(state.message ?: "Произошла ошибка")
            }
        } else if (state is Resource.Success && state.data != null) {
            // Можно добавить сообщение об успешном сохранении, если нужно
        }
    }

    ProfileScreenContent(
        profileState = profileState,
        username = username,
        bio = bio,
        snackbarHostState = snackbarHostState,
        onUsernameChange = viewModel::onUsernameChange,
        onBioChange = viewModel::onBioChange,
        onSaveClick = viewModel::onSaveProfileClicked,
        onRetry = viewModel::loadProfile
    )
}

// Stateless-компонент
@Composable
fun ProfileScreenContent(
    profileState: Resource<UserProfile>,
    username: String,
    bio: String,
    snackbarHostState: SnackbarHostState,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (profileState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Не удалось загрузить профиль")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Попробовать снова")
                        }
                    }
                }
                is Resource.Success -> {
                    val userProfile = profileState.data
                    if (userProfile != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Аватар (пока плейсхолдер)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = userProfile.email, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = username,
                                onValueChange = onUsernameChange,
                                label = { Text("Имя пользователя") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = bio,
                                onValueChange = onBioChange,
                                label = { Text("О себе") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) {
                                Text("Сохранить изменения")
                            }
                        }
                    }
                }
            }
        }
    }
}