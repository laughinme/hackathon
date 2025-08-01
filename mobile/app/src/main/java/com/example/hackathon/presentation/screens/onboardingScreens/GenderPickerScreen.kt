package com.example.hackathon.presentation.screens.onboardingScreens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.PreviewTheme

@Composable
fun GenderPickerScreen(onNext: () -> Unit) {
    val genders = listOf("Male", "Female", "Other")
    //var selectedGender TODO реализовать эту переменную во ViewModel
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "Choose your gender", fontSize = 30.sp)
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                genders.forEach { gender ->
                    OutlinedButton(
                        onClick = {},//TODO <- тут вставить функцию из ViewModel для изменения selectedGender
                        modifier = Modifier.width(110.dp).height(55.dp)
                    ) {
                        Text(text = gender)
                    }
                }
            }
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(bottom = 60.dp), contentAlignment = Alignment.BottomCenter){
            Button(modifier = Modifier.height(60.dp).fillMaxWidth(0.9f), onClick = onNext) {
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
fun GenderPickerPreview() {
    PreviewTheme {
        GenderPickerScreen(
            onNext = {}
        )
    }
}
