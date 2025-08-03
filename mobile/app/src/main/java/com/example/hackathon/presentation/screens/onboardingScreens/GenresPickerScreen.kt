package com.example.hackathon.presentation.screens.onboardingScreens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.PreviewTheme
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.presentation.viewmodel.ProfileEvent
import com.example.hackathon.presentation.viewmodel.ProfileViewModel

@Composable
fun GenresPickerScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onProfileComplete: () -> Unit
) {
    val allGenresState by viewModel.allGenresState.collectAsStateWithLifecycle()
    val selectedGenres by viewModel.selectedGenres.collectAsStateWithLifecycle()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val onboardingSaveComplete by viewModel.onboardingSaveComplete.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = onboardingSaveComplete) {
        if (onboardingSaveComplete) {
            onProfileComplete()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Выберите любимые жанры", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 16.dp))
            }
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val state = allGenresState) {
                    is Resource.Loading -> CircularProgressIndicator()
                    is Resource.Error -> Text(state.message ?: "Ошибка загрузки", color = MaterialTheme.colorScheme.error)
                    is Resource.Success -> {
                        val genres = state.data ?: emptyList()
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            genres.forEach { genre ->
                                FilterChip(
                                    selected = genre in selectedGenres,
                                    onClick = {
                                        // Отправляем правильное событие с объектом Genre
                                        viewModel.onEvent(ProfileEvent.OnGenreChange(genre))
                                    },
                                    label = { Text(genre.name) }
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(bottom = 60.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    modifier = Modifier.height(60.dp).fillMaxWidth(0.9f),
                    onClick = {
                        // Вызываем универсальное событие сохранения
                        viewModel.onEvent(ProfileEvent.OnSaveClick)
                    },
                    enabled = profileState !is Resource.Loading && selectedGenres.isNotEmpty()
                ) {
                    Text("Завершить", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        if (profileState is Resource.Loading) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Theme"
)
@Composable
fun GenresPreview() {
    PreviewTheme {
        GenresPickerScreen(
            onProfileComplete = {}
        )
    }
}