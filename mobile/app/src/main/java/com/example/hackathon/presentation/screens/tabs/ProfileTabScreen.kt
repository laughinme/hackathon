package com.example.hackathon.presentation.screens.tabs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.hackathon.R
import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.util.uriToFile
import com.example.hackathon.presentation.viewmodel.ProfileEvent
import com.example.hackathon.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// --- STATEFUL COMPONENT (HOLDER) ---

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
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isPublic by viewModel.isPublic.collectAsStateWithLifecycle()
    val avatarUrl by viewModel.avatarUrl.collectAsStateWithLifecycle()
    val latitude by viewModel.latitude.collectAsStateWithLifecycle()
    val longitude by viewModel.longitude.collectAsStateWithLifecycle()

    val allGenresState by viewModel.allGenresState.collectAsStateWithLifecycle()
    val selectedGenres by viewModel.selectedGenres.collectAsStateWithLifecycle()

    val allCitiesState by viewModel.allCitiesState.collectAsStateWithLifecycle()
    val selectedCityId by viewModel.selectedCityId.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Launcher для выбора изображения
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uriToFile(context, it)?.let { file ->
                viewModel.onEvent(ProfileEvent.OnPictureSelected(file))
            }
        }
    }

    // TODO: Реализовать получение геолокации и вызов onEvent(ProfileEvent.OnLocationUpdate(...))

    // Отслеживаем состояние выхода для навигации
    LaunchedEffect(key1 = logoutState) {
        if (logoutState is Resource.Success) {
            onLogoutSuccess()
        }
    }

    // Показываем Snackbar при ошибках
    LaunchedEffect(key1 = profileState, key2 = logoutState) {
        val profileError = (profileState as? Resource.Error)?.message
        val logoutError = (logoutState as? Resource.Error)?.message
        val errorMessage = profileError ?: logoutError
        if (errorMessage != null) {
            scope.launch { snackbarHostState.showSnackbar(errorMessage) }
        }
    }

    ProfileScreenContent(
        profileResource = profileState,
        snackbarHostState = snackbarHostState,
        username = username,
        bio = bio,
        birthDate = birthDate,
        language = language,
        isPublic = isPublic,
        avatarUrl = avatarUrl,
        latitude = latitude,
        longitude = longitude,
        allGenresState = allGenresState,
        selectedGenres = selectedGenres,
        allCitiesState = allCitiesState,
        selectedCityId = selectedCityId,
        onEvent = viewModel::onEvent,
        onAvatarClick = { imagePickerLauncher.launch("image/*") }
    )
}

// --- STATELESS COMPONENT (PURE UI) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    profileResource: Resource<out Any>,
    snackbarHostState: SnackbarHostState,
    username: String,
    bio: String,
    birthDate: LocalDate?,
    language: String,
    isPublic: Boolean,
    avatarUrl: String?,
    latitude: Double?,
    longitude: Double?,
    allGenresState: Resource<List<Genre>>,
    selectedGenres: Set<Genre>,
    allCitiesState: Resource<List<City>>,
    selectedCityId: Int?,
    onEvent: (ProfileEvent) -> Unit,
    onAvatarClick: () -> Unit
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // --- Аватар ---
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clickable { onAvatarClick() },
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Аватар",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.ic_launcher_background), // Замени на свою заглушку
                                error = painterResource(id = R.drawable.ic_launcher_background) // Замени на свою заглушку
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                                    .padding(4.dp)
                            )
                        }

                        // --- Основные поля ---
                        OutlinedTextField(value = username, onValueChange = { onEvent(ProfileEvent.OnUsernameChange(it)) }, label = { Text("Имя пользователя") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = bio, onValueChange = { onEvent(ProfileEvent.OnBioChange(it)) }, label = { Text("О себе") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                        OutlinedTextField(value = language, onValueChange = { onEvent(ProfileEvent.OnLanguageChange(it)) }, label = { Text("Язык (напр. ru)") }, modifier = Modifier.fillMaxWidth())

                        // --- Выбор даты рождения ---
                        DatePickerField(selectedDate = birthDate, onDateSelected = { onEvent(ProfileEvent.OnBirthDateChange(it)) })

                        // --- Выбор города ---
                        CitySelector(allCitiesState = allCitiesState, selectedCityId = selectedCityId, onEvent = onEvent)

                        // --- Выбор жанров ---
                        GenreSelector(allGenresState = allGenresState, selectedGenres = selectedGenres, onEvent = onEvent)

                        // --- Настройки приватности и геолокации ---
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Профиль публичный", style = MaterialTheme.typography.bodyLarge)
                            Switch(checked = isPublic, onCheckedChange = { onEvent(ProfileEvent.OnIsPublicChange(it)) })
                        }
                        Text("Координаты: ${latitude ?: "Неизвестно"}, ${longitude ?: "Неизвестно"}", style = MaterialTheme.typography.bodySmall)

                        // --- Кнопки действий ---
                        Button(onClick = { onEvent(ProfileEvent.OnSaveClick) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Сохранить изменения")
                        }
                        OutlinedButton(
                            onClick = { onEvent(ProfileEvent.OnLogoutClick) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Text("Выйти из аккаунта")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    OutlinedTextField(
        value = selectedDate?.format(formatter) ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Text("Дата рождения") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: Instant.now().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateSelected(date)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { Button(onClick = { showDatePicker = false }) { Text("Отмена") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CitySelector(
    allCitiesState: Resource<List<City>>,
    selectedCityId: Int?,
    onEvent: (ProfileEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    when(allCitiesState) {
        is Resource.Loading -> Text("Загрузка городов...")
        is Resource.Error -> Text(allCitiesState.message ?: "Ошибка загрузки городов", color = MaterialTheme.colorScheme.error)
        is Resource.Success -> {
            val cities = allCitiesState.data ?: emptyList()
            val selectedCityName = cities.find { it.id == selectedCityId }?.name ?: ""

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCityName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Город") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city.name) },
                            onClick = {
                                onEvent(ProfileEvent.OnCityChange(city))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenreSelector(
    allGenresState: Resource<List<Genre>>,
    selectedGenres: Set<Genre>,
    onEvent: (ProfileEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Любимые жанры", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        when(allGenresState) {
            is Resource.Loading -> Text("Загрузка жанров...")
            is Resource.Error -> Text(allGenresState.message ?: "Ошибка загрузки жанров", color = MaterialTheme.colorScheme.error)
            is Resource.Success -> {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    (allGenresState.data ?: emptyList()).forEach { genre ->
                        FilterChip(
                            selected = genre in selectedGenres,
                            onClick = { onEvent(ProfileEvent.OnGenreChange(genre)) },
                            label = { Text(genre.name) }
                        )
                    }
                }
            }
        }
    }
}
