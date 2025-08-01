package com.example.hackathon.presentation.screens.onboardingScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.example.compose.PreviewTheme
import java.time.LocalDateTime

// USES https://github.com/commandiron/WheelPickerCompose

@Composable
fun AgePickerScreen() {
    WheelDateTimePicker(
        startDateTime = LocalDateTime.of(
            2025, 10, 20, 5, 30
        ),
        minDateTime = LocalDateTime.now(),
        maxDateTime = LocalDateTime.of(
            2025, 10, 20, 5, 30
        ),
        timeFormat = TimeFormat.AM_PM,
        size = DpSize(200.dp, 100.dp),
        rowCount = 5,
        textStyle = MaterialTheme.typography.titleSmall,
        textColor = Color(0xFFffc300),
        selectorProperties = WheelPickerDefaults.selectorProperties(
            enabled = true,
            shape = RoundedCornerShape(0.dp),
            color = Color(0xFFf1faee).copy(alpha = 0.2f),
            border = BorderStroke(2.dp, Color(0xFFf1faee))
        )
    ) { }
}

@Preview(showBackground = true, name = "Light Theme")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Theme"
)
@Composable
fun AgePickerPreview() {
    PreviewTheme {
        AgePickerScreen()
    }
}
