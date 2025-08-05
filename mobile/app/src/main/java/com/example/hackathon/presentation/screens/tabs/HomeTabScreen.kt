package com.example.hackathon.presentation.screens.tabs

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.presentation.components.BookCard
import com.example.hackathon.presentation.components.BookDetailsBottomSheet
import com.example.hackathon.presentation.viewmodel.HomePageViewModel
import com.example.hackathon.presentation.viewmodel.InteractionEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabScreen(viewModel: HomePageViewModel = hiltViewModel()) {

    val booksForYouState by viewModel.booksForYouState.collectAsState()
    val selectedBook by viewModel.selectedBook.collectAsState()
    val interactionEvent by viewModel.interactionEvent.collectAsState()
    val context = LocalContext.current

    // Обработка одноразовых событий (Toast)
    LaunchedEffect(interactionEvent) {
        when (val event = interactionEvent) {
            is InteractionEvent.ShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                viewModel.onInteractionEventConsumed() // Сбрасываем событие
            }
            null -> {}
        }
    }

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
                                    viewModel.onBookCardClicked(book)
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
    val currentSelectedBook = selectedBook
    if (currentSelectedBook != null) {
        BookDetailsBottomSheet(
            book = currentSelectedBook,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismiss = { viewModel.onBottomSheetDismissed() },
            onLikeClick = { viewModel.likeSelectedBook() },
            onReserveClick = { viewModel.reserveSelectedBook() }
        )
    }
}


