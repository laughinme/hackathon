package com.example.hackathon.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.PreviewTheme
import com.example.hackathon.domain.model.BookCondition
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.util.uriToFile
import com.example.hackathon.presentation.viewmodel.AddBookEvent
import com.example.hackathon.presentation.viewmodel.AddBookViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    viewModel: AddBookViewModel = hiltViewModel(),
    onBookCreatedSuccessfully: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Лаунчер для выбора нескольких изображений
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val files = uris.mapNotNull { uri ->
            uriToFile(context, uri)
        }
        viewModel.onEvent(AddBookEvent.OnPhotosSelected(files))
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
                        genres = if (state.genresState is Resource.Success) state.genresState.data ?: emptyList() else emptyList(),
                        selectedGenre = state.selectedGenre,
                        onGenreSelected = { viewModel.onEvent(AddBookEvent.OnGenreSelect(it)) },
                        isLoading = state.genresState is Resource.Loading
                    )
                }
                item {
                    ConditionDropdown(
                        selectedCondition = state.selectedCondition,
                        onConditionSelected = { viewModel.onEvent(AddBookEvent.OnConditionSelect(it)) }
                    )
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

            if (state.creationState is Resource.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
            value = selectedCondition?.name?.lowercase()?.replaceFirstChar { it.titlecase() } ?: "Выберите состояние",
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
                    text = { Text(condition.name.lowercase().replaceFirstChar { it.titlecase() }) },
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

