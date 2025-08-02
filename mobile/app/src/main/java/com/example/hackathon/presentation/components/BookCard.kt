package com.example.hackathon.presentation.components

import android.provider.SyncStateContract
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BookCard() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Card(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            Column() {
                Box(modifier = Modifier.fillMaxWidth().weight(0.7f)) {

                }
                Box(modifier = Modifier.fillMaxWidth().weight(0.3f)) {

                }
            }
        }
    }
}