package com.example.hackathon.presentation.screens.tabs

import androidx.compose.foundation.BorderStroke
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
import com.example.hackathon.presentation.viewmodel.ProfileEvent
import com.example.hackathon.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun ProfileTabScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit
) {
    // Собираем все состояния из ViewModel
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val logoutState by viewModel.logoutState.collectAsStateWithLifecycle()
    val username by viewModel.username.collectAsStateWithLifecycle()
    val bio by viewModel.bio.collectAsStateWithLifecycle()
    val birthDate by viewModel.birthDate.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val selectedGenres by viewModel.selectedGenres.collectAsStateWithLifecycle()
    val allGenres by viewModel.allGenres.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Отслеживаем состояние выхода для навигации
    LaunchedEffect(key1 = logoutState) {
        when (val state = logoutState) {
            is Resource.Success -> onLogoutSuccess()
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message ?: "Ошибка выхода") }
            }
            else -> Unit
        }
    }

    // Отслеживаем состояние загрузки профиля для показа ошибок
    LaunchedEffect(key1 = profileState) {
        if (profileState is Resource.Error) {
            scope.launch { snackbarHostState.showSnackbar(profileState.message ?: "Произошла ошибка") }
        }
    }

    ProfileScreenContent(
        profileResource = profileState,
        logoutResource = logoutState,
        snackbarHostState = snackbarHostState,
        username = username,
        bio = bio,
        birthDate = birthDate,
        city = city,
        allGenres = allGenres,
        selectedGenres = selectedGenres,
        onEvent = viewModel::onEvent
    )
}

// --- STATELESS-КОМПОНЕНТ (ЧИСТЫЙ UI) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    profileResource: Resource<UserProfile>,
    logoutResource: Resource<Unit>?,
    snackbarHostState: SnackbarHostState,
    username: String,
    bio: String,
    birthDate: LocalDate?,
    city: String,
    allGenres: List<String>,
    selectedGenres: Set<String>,
    onEvent: (ProfileEvent) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Профиль") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (profileResource) {
                is Resource.Loading -> CircularProgressIndicator()
                is Resource.Error -> {
                    Button(onClick = { onEvent(ProfileEvent.OnRetry) }) {
                        Text("Попробовать снова")
                    }
                }
                is Resource.Success -> {
                    val userProfile = profileResource.data
                    if (userProfile != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = userProfile.email, style = MaterialTheme.typography.titleMedium)

                            OutlinedTextField(
                                value = username,
                                onValueChange = { onEvent(ProfileEvent.OnUsernameChange(it)) },
                                label = { Text("Имя пользователя") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = bio,
                                onValueChange = { onEvent(ProfileEvent.OnBioChange(it)) },
                                label = { Text("О себе") },
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            )

                            // TODO: Добавить DatePickerDialog для выбора даты рождения
                            // TODO: Добавить Dropdown для выбора города

                            Text("Любимые жанры", style = MaterialTheme.typography.titleMedium)
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                allGenres.forEach { genre ->
                                    FilterChip(
                                        selected = genre in selectedGenres,
                                        onClick = { onEvent(ProfileEvent.OnGenreSelected(genre)) },
                                        label = { Text(genre) }
                                    )
                                }
                            }

                            Button(
                                onClick = { onEvent(ProfileEvent.OnSaveProfileClick) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Сохранить изменения")
                            }

                            OutlinedButton(
                                onClick = { onEvent(ProfileEvent.OnLogoutClick) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = logoutResource !is Resource.Loading,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                            ) {
                                if (logoutResource is Resource.Loading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                } else {
                                    Text("Выйти из аккаунта")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
