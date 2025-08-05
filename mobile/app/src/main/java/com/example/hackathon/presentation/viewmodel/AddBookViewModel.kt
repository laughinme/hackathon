package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.BookCondition
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.usecase.CreateBookUseCase
import com.example.hackathon.domain.usecase.GetGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

// --- КОНТРАКТ (STATE, EVENT) ---

data class AddBookState(
    // Поля формы
    val title: String = "",
    val description: String = "",
    val author: String = "",
    val pages: String = "",
    val selectedGenre: Genre? = null,
    val selectedCondition: BookCondition? = null,
    val selectedExchangeLocation: String = "",
    val selectedPhotos: List<File> = emptyList(),

    // Состояние асинхронных операций
    val genresState: Resource<List<Genre>> = Resource.Loading(),
    val creationState: Resource<Book>? = null // Для отслеживания процесса создания
)

sealed interface AddBookEvent {
    data class OnTitleChange(val value: String) : AddBookEvent
    data class OnDescriptionChange(val value: String) : AddBookEvent
    data class OnAuthorChange(val value: String) : AddBookEvent
    data class OnPagesChange(val value: String) : AddBookEvent
    data class OnGenreSelect(val genre: Genre) : AddBookEvent
    data class OnConditionSelect(val condition: BookCondition) : AddBookEvent
    data class OnExchangeLocationSelect(val locationId: String) : AddBookEvent
    data class OnPhotosSelected(val files: List<File>) : AddBookEvent
    object OnCreateClick : AddBookEvent
    object ResetCreationStatus : AddBookEvent // Для сброса состояния после навигации или показа сообщения
}


// --- VIEWMODEL ---

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val createBookUseCase: CreateBookUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddBookState())
    val state = _state.asStateFlow()

    init {
        loadGenres()
    }

    fun onEvent(event: AddBookEvent) {
        when (event) {
            is AddBookEvent.OnTitleChange -> _state.update { it.copy(title = event.value) }
            is AddBookEvent.OnDescriptionChange -> _state.update { it.copy(description = event.value) }
            is AddBookEvent.OnAuthorChange -> _state.update { it.copy(author = event.value) }
            is AddBookEvent.OnPagesChange -> _state.update { it.copy(pages = event.value) }
            is AddBookEvent.OnGenreSelect -> _state.update { it.copy(selectedGenre = event.genre) }
            is AddBookEvent.OnConditionSelect -> _state.update { it.copy(selectedCondition = event.condition) }
            is AddBookEvent.OnExchangeLocationSelect -> _state.update { it.copy(selectedExchangeLocation = event.locationId) }
            is AddBookEvent.OnPhotosSelected -> _state.update { it.copy(selectedPhotos = event.files) }
            is AddBookEvent.OnCreateClick -> createBook()
            is AddBookEvent.ResetCreationStatus -> _state.update { it.copy(creationState = null) }
        }
    }

    private fun loadGenres() {
        getGenresUseCase().onEach { result ->
            _state.update { it.copy(genresState = result) }
        }.launchIn(viewModelScope)
    }

    private fun createBook() {
        // TODO: Добавить валидацию полей перед отправкой
        val params = CreateBookParams(
            title = _state.value.title,
            description = _state.value.description.takeIf { it.isNotBlank() },
            extraTerms = null,
            authorId = 1, // TODO: Заменить на реальный ID автора
            genreId = _state.value.selectedGenre?.id ?: 1, // TODO: Обработать ошибку, если не выбран
            language = "ru",
            pages = _state.value.pages.toIntOrNull(),
            condition = _state.value.selectedCondition ?: BookCondition.NORMAL,
            exchangeLocationId = _state.value.selectedExchangeLocation.toInt(),
            photos = _state.value.selectedPhotos
        )

        createBookUseCase(params).onEach { result ->
            _state.update { it.copy(creationState = result) }
        }.launchIn(viewModelScope)
    }
}