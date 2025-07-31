package com.example.hackathon.presentation.screens.onboardingScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.PreviewTheme

@Composable
fun SignInScreen() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = "TODO email",
                    onValueChange = {},
                    label = { Text("Email") },
                    placeholder = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = "TODO password",
                    onValueChange = {},
                    label = { Text("Password") },
                    placeholder = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(50.dp))
                Button(modifier = Modifier.width(160.dp), onClick = {
                        //TODO Сделать регестрацию и переход на AgePickerScreen
                }) {
                    Text("Sign In")
                }
                Spacer(modifier = Modifier.height(15.dp))
                TextButton(onClick = {
                    //Todo Сделать переход на SignUpScreen
                }) {
                    Text("Sign Up")
                }
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
fun SignInPreview() {
    PreviewTheme {
        SignInScreen()
    }
}
