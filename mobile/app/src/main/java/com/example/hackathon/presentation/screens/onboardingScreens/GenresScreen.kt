package com.example.hackathon.presentation.screens.onboardingScreens

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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.PreviewTheme

@Composable
fun GenresScreen() {
    val allGenres = listOf("Рок", "Джаз", "Хип-хоп", "Классика", "Поп", "Электроника", "Регги")
    //val selecredGenres TODO реализовать во ViewModel тип данных set
    val testBools = listOf(true, false)
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center) {
            Text("Choose your favorite genres",
                fontSize = 25.sp)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                allGenres.forEach { genre ->
                    FilterChip(
                        selected = testBools.random(), //TODO заменить на genre in selectedGenres.value
                        onClick = {
                            //TODO здесь использовать функцию из ViewModel для изменения selectedGenres
                            //Пример:             val currentSelected = selectedGenres.value.toMutableSet()
                            //                    if (genre in currentSelected) {
                            //                        currentSelected.remove(genre)
                            //                    } else {
                            //                        currentSelected.add(genre)
                            //                    }
                            //                    selectedGenres.value = currentSelected
                            //                },
                        },
                        label = { Text(genre) }
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(bottom = 60.dp),
            contentAlignment = Alignment.BottomCenter){
            Button(modifier = Modifier.height(60.dp).fillMaxWidth(0.9f), onClick = {


                //TODO  Миша сделай тут переход на HomeScreen


            }) {
                Text("Continue",
                    style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}


@Preview(showBackground = true, name = "Light Theme")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Theme"
)
@Composable
fun GenresPreview() {
    PreviewTheme {
        GenresScreen()
    }
}