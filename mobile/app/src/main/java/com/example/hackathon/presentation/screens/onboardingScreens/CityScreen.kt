package com.example.hackathon.presentation.screens.onboardingScreens


import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(onNext: () -> Unit) {
    val cities = listOf("Новосибирск", "Москва", "Санкт-Петербург")
    //var expanded
    //var selectedCity TODO Реализовать эти две переменные во ViewModel

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f), contentAlignment = Alignment.Center) {
            Text("Choose your city", fontSize = 25.sp)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = false, //true заменить на expanded
                onExpandedChange = {} // <- TODO Сюда вставить функцию для изменения состояния expended из ViewModel
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    value = "Новосибирск", //TODO Поменять на selectedCity
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }, //true заменить на expanded
                )
                ExposedDropdownMenu(expanded = false, onDismissRequest = {}) {
                    cities.forEach { city ->
                        DropdownMenuItem(text = { Text(city) }, onClick = { /* ... */ })
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 60.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(0.9f), onClick = onNext) {
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
fun CityPreview() {
    PreviewTheme {
        CityScreen(
            onNext = {}
        )
    }
}



