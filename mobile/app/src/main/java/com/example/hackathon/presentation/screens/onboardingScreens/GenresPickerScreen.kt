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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.PreviewTheme

@Composable
fun GenresPickerScreen(onProfileComplete: () -> Unit) {
    val allGenres = listOf("Рок", "Джаз", "Хип-хоп", "Классика", "Поп", "Электроника", "Регги")
    //val selecredGenres TODO реализовать во ViewModel тип данных set
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Text("Choose your favorite genres", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 16.dp))
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), maxItemsInEachRow = 3) {
                allGenres.forEach { genre ->
                    FilterChip(
                        selected = false, //TODO заменить на genre in selectedGenres.value
                        onClick = { /* TODO */ },
                        label = { Text(genre) }
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(bottom = 60.dp), contentAlignment = Alignment.BottomCenter){
            Button(modifier = Modifier.height(60.dp).fillMaxWidth(0.9f), onClick = onProfileComplete) {
                Text("Continue", style = MaterialTheme.typography.headlineSmall)
            }
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