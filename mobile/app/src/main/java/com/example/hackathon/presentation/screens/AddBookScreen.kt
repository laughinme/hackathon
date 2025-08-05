package com.example.hackathon.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.PreviewTheme
import com.example.hackathon.domain.model.BookCondition
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.util.uriToFile
import com.example.hackathon.presentation.components.Map
import com.example.hackathon.presentation.viewmodel.AddBookEvent
import com.example.hackathon.presentation.viewmodel.AddBookViewModel
import com.example.hackathon.presentation.viewmodel.MapViewModel
import kotlinx.coroutines.launch

const val ADDBOOK_SCREEN_TAG = "AddBookScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    viewModel: AddBookViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    onBookCreatedSuccessfully: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isMapFullScreen by remember { mutableStateOf(false) }

    val exchangePoints by mapViewModel.exchangePoints.collectAsStateWithLifecycle()
    val currentLocation by mapViewModel.currentLocation.collectAsStateWithLifecycle()
    val selectedLocationInfo by mapViewModel.selectedLocationInfo.collectAsStateWithLifecycle()
    val pickedLocation by mapViewModel.pickedLocation.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false // Разрешаем частичное раскрытие
    )


    // Лаунчер для выбора нескольких изображений
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val files = uris.mapNotNull { uri ->
            uriToFile(context, uri)
        }
        viewModel.onEvent(AddBookEvent.OnPhotosSelected(files))
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Разрешение получено
            // Здесь можно выполнять действия, требующие геолокации
            Log.d(ADDBOOK_SCREEN_TAG, "Разрешение на геолокацию получено!")
            mapViewModel.getCurrentLocation()
        } else {
            // Разрешение отклонено
            Log.d(ADDBOOK_SCREEN_TAG, "Разрешение на геолокацию отклонено.")
            (context as? Activity)?.finish()
        }
    }

    LaunchedEffect(Unit) {
        when {
            // Проверяем, есть ли уже разрешение
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешение уже есть
                Log.d(ADDBOOK_SCREEN_TAG, "Разрешение уже есть. Можно получать геолокацию.")
                mapViewModel.getCurrentLocation()
            }
            // Запрашиваем разрешение, если его нет
            else -> {
                // Запускаем запрос разрешения
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }



        LaunchedEffect(Unit) {
            org.osmdroid.config.Configuration.getInstance()
                .load(context, PreferenceManager.getDefaultSharedPreferences(context))
        }


        // Обработка состояний загрузки жанров и создания книги
        LaunchedEffect(state.genresState, state.creationState) {
            val genresState = state.genresState
            if (genresState is Resource.Error) {
                scope.launch {
                    snackbarHostState.showSnackbar(genresState.message ?: "Ошибка загрузки жанров")
                }
            }

            val creationState = state.creationState
            if (creationState is Resource.Error) {
                scope.launch {
                    snackbarHostState.showSnackbar(creationState.message ?: "Ошибка создания книги")
                }
            } else if (creationState is Resource.Success) {
                // Показываем сообщение и переходим на другой экран
                scope.launch {
                    snackbarHostState.showSnackbar("Книга успешно добавлена!")
                }
                onBookCreatedSuccessfully()
                viewModel.onEvent(AddBookEvent.ResetCreationStatus)
            }
        }

        if (isMapFullScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Map(
                    context = context,
                    locations = exchangePoints,
                    currentLocation = currentLocation,
                    onLocationClick = { location ->
                        mapViewModel.onMarkerClicked(location)
                        isMapFullScreen = false
                    }
                )
            }
        } else {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    TopAppBar(title = { Text("Добавить книгу") })
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            OutlinedTextField(
                                value = state.title,
                                onValueChange = { viewModel.onEvent(AddBookEvent.OnTitleChange(it)) },
                                label = { Text("Название книги") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = state.author,
                                onValueChange = { viewModel.onEvent(AddBookEvent.OnAuthorChange(it)) },
                                label = { Text("Автор") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = state.description,
                                onValueChange = { viewModel.onEvent(AddBookEvent.OnDescriptionChange(it)) },
                                label = { Text("Описание") },
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = state.pages,
                                onValueChange = { viewModel.onEvent(AddBookEvent.OnPagesChange(it)) },
                                label = { Text("Количество страниц") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                        item {
                            GenreDropdown(
                                genres = if (state.genresState is Resource.Success) state.genresState.data
                                    ?: emptyList() else emptyList(),
                                selectedGenre = state.selectedGenre,
                                onGenreSelected = { viewModel.onEvent(AddBookEvent.OnGenreSelect(it)) },
                                isLoading = state.genresState is Resource.Loading
                            )
                        }
                        item {
                            ConditionDropdown(
                                selectedCondition = state.selectedCondition,
                                onConditionSelected = {
                                    viewModel.onEvent(
                                        AddBookEvent.OnConditionSelect(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = pickedLocation ?: "Удобная точка обмена",
                                enabled = false,
                                onValueChange = {},
                                label = {
                                    Text("Удобная точка обмена")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        isMapFullScreen = true
                                    }),
                                singleLine = true
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { isMapFullScreen = true }
                                ) {
                                    Map(
                                        context = context,
                                        locations = exchangePoints,
                                        currentLocation = currentLocation,
                                        onLocationClick = { /* Ничего не делаем в свёрнутом виде */ }
                                    )
                                }
                            }
                        }
                        item {
                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Выбрать фото (${state.selectedPhotos.size})")
                            }
                        }
                        item {
                            Button(
                                onClick = { viewModel.onEvent(AddBookEvent.OnCreateClick) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = state.creationState !is Resource.Loading
                            ) {
                                Text("Добавить книгу")
                            }
                        }
                    }

                    if (selectedLocationInfo != null) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                mapViewModel.dismissLocationInfo()
                            },
                            sheetState = sheetState
                        ) {
                            selectedLocationInfo?.let { selectedLocationInfo ->
                                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    Text(
                                        text = selectedLocationInfo.title,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Адрес: ${selectedLocationInfo.address}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Город: ${selectedLocationInfo.city.name}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Статус: ${if (selectedLocationInfo.isActive) "Активен" else "Закрыт"}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    sheetState.hide()            // сначала плавно прячем
                                                } finally {
                                                    // этот блок выполнится и если hide() отменится
                                                    viewModel.onEvent(
                                                        AddBookEvent.OnExchangeLocationSelect(selectedLocationInfo.id.toString())
                                                    )
                                                    mapViewModel.onPickedLocation()
                                                    mapViewModel.dismissLocationInfo()
                                                }
                                            }
                                        }
                                    ) { Text("Добавить") }
                                }
                            }
                        }
                    }
                if (state.creationState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenreDropdown(
        genres: List<Genre>,
        selectedGenre: Genre?,
        onGenreSelected: (Genre) -> Unit,
        isLoading: Boolean,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGenre?.name ?: "Выберите жанр",
                onValueChange = {},
                readOnly = true,
                label = { Text("Жанр") },
                trailingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genres.forEach { genre ->
                    DropdownMenuItem(
                        text = { Text(genre.name) },
                        onClick = {
                            onGenreSelected(genre)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConditionDropdown(
        selectedCondition: BookCondition?,
        onConditionSelected: (BookCondition) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }
        val conditions = BookCondition.values()

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCondition?.name?.lowercase()?.replaceFirstChar { it.titlecase() }
                    ?: "Выберите состояние",
                onValueChange = {},
                readOnly = true,
                label = { Text("Состояние") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                conditions.forEach { condition ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                condition.name.lowercase().replaceFirstChar { it.titlecase() })
                        },
                        onClick = {
                            onConditionSelected(condition)
                            expanded = false
                        }
                    )
                }
            }
        }
    }


    @Preview(showBackground = true, name = "Light Theme")
    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
    @Composable
    fun AddBookScreenPreview() {
        PreviewTheme {
            AddBookScreen(onBookCreatedSuccessfully = {})
        }
    }

