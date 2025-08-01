package com.example.hackathon.presentation.screens.onboardingScreens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.PreviewTheme

@Composable
fun GenderPickerScreen() {
    val genders = listOf("Male", "Female", "Other")
    //var selectedGender TODO реализовать эту переменную во ViewModel
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                genders.forEach { gender ->
                    //val isSelected = gender == selectedGender
                    OutlinedButton(
                        onClick = {},//TODO <- тут вставить функцию из ViewModel для изменения selectedGender
                        colors = ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier.width(100.dp)
                        //colors = if (isSelected) {
                        //  ButtonDefaults.outlinedButtonColors(
                        //                        containerColor = MaterialTheme.colorScheme.primary,
                        //                        contentColor = MaterialTheme.colorScheme.onPrimary
                        //                    )
                        //} else {
                        //  ButtonDefaults.outlinedButtonColors()
                        //},
                        //modifier = Modifier.offset(x = (-1 * genders.indexOf(gender)).dp, zIndex = if (isSelected) 1f else 0f)
                        //TODO все модификаторы сверху раскомментировать после добавления ViewModel, а старые удалить
                    ) {
                        Text(text = gender)
                    }
                }
            }
        }
        Box(modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter){
            Button(modifier = Modifier.height(60.dp).fillMaxWidth(0.9f).padding(bottom = 10.dp), onClick = {
                //TODO  Миша сделай тут переход на SignInScreen
            }) {
                Text("Get Started",
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
fun GenderPickerPreview() {
    PreviewTheme {
        GenderPickerScreen()
    }
}
