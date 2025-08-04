package com.example.hackathon.presentation.screens.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.presentation.components.BookCard
import com.example.hackathon.presentation.components.BookDetailsBottomSheet
import com.example.hackathon.presentation.viewmodel.HomePageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabScreen(viewModel: HomePageViewModel = hiltViewModel()) {

    val booksForYouState by viewModel.booksForYouState.collectAsState()

    // Состояние для управления bottom sheet
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (val state = booksForYouState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val books = state.data
                if (books.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No books for you at the moment.", fontSize = 18.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(books) { book ->
                            BookCard(
                                book = book,
                                onItemClick = {
                                    // При клике обновляем выбранную книгу
                                    selectedBook = book
                                }
                            )
                        }
                    }
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}", fontSize = 18.sp, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    // Показываем BottomSheet, если книга выбрана
    if (selectedBook != null) {
        BookDetailsBottomSheet(
            book = selectedBook!!,
            sheetState = sheetState,
            onDismiss = {
                // Скрываем sheet и сбрасываем книгу
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    selectedBook = null
                }
            }
        )
    }
}

