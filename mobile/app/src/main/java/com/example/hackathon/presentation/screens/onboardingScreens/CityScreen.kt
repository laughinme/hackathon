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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.PreviewTheme
import com.example.hackathon.presentation.viewmodel.onboardingViewModel.CityPickerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    viewModel: CityPickerViewModel = hiltViewModel(),
    onNext: () -> Unit
) {

    val cities = listOf("Новосибирск", "Москва", "Санкт-Петербург")

    val expanded by viewModel.expanded.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
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
                expanded = expanded,
                onExpandedChange = {
                    viewModel.onExpandedChanged()
                }
            ) {
                TextField(
                    modifier = Modifier
                        //.menuAnchor() вроде устаревшая какая та тема
                        .fillMaxWidth(),
                    value = selectedCity,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {}) {
                    cities.forEach { city ->
                        DropdownMenuItem(text = { Text(city) }, onClick = {
                            viewModel.onCitySelected(city = city)
                        })
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
            Button(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.9f), onClick = {
                    viewModel.onCityClicked()
                    onNext()
                }) {
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



