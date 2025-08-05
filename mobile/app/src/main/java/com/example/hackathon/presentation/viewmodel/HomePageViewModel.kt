package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.ReserveBookParams
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.usecase.GetBooksForYouUseCase
import com.example.hackathon.domain.usecase.LikeBookUseCase
import com.example.hackathon.domain.usecase.RecordClickUseCase
import com.example.hackathon.domain.usecase.ReserveBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val getBooksForYouUseCase: GetBooksForYouUseCase,
    private val recordClickUseCase: RecordClickUseCase,
    private val likeBookUseCase: LikeBookUseCase,
    private val reserveBookUseCase: ReserveBookUseCase
) : ViewModel() {

    // Состояние для списка книг
    private val _booksForYouState = MutableStateFlow<Resource<List<Book>>>(Resource.Loading())
    val booksForYouState: StateFlow<Resource<List<Book>>> = _booksForYouState.asStateFlow()

    // Состояние для выбранной книги (для BottomSheet)
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()

    // Состояние для отображения одноразовых событий (например, Toast)
    private val _interactionEvent = MutableStateFlow<InteractionEvent?>(null)
    val interactionEvent: StateFlow<InteractionEvent?> = _interactionEvent.asStateFlow()


    init {
        fetchBooksForYou()
    }

    fun fetchBooksForYou(limit: Int? = null) {
        getBooksForYouUseCase(limit)
            .onEach { result ->
                _booksForYouState.value = result
            }
            .launchIn(viewModelScope)
    }

    /**
     * Вызывается при клике на карточку книги.
     * Устанавливает книгу как выбранную и записывает клик.
     */
    fun onBookCardClicked(book: Book) {
        _selectedBook.value = book
        // Запускаем запись клика в фоне, результат нам не важен на UI
        recordClickUseCase(book.id).launchIn(viewModelScope)
    }

    /**
     * Вызывается при закрытии BottomSheet.
     */
    fun onBottomSheetDismissed() {
        _selectedBook.value = null
    }

    /**
     * Вызывается при нажатии на кнопку "Лайк".
     */
    fun likeSelectedBook() {
        _selectedBook.value?.let { book ->
            viewModelScope.launch {
                likeBookUseCase(book.id).collect { result ->
                    if (result is Resource.Success) {
                        _interactionEvent.value = InteractionEvent.ShowToast("Книга '${book.title}' добавлена в избранное!")
                    } else if (result is Resource.Error) {
                        _interactionEvent.value = InteractionEvent.ShowToast("Ошибка: ${result.message}")
                    }
                }
            }
        }
    }

    /**
     * Вызывается при нажатии на кнопку "Резервировать".
     */
    fun reserveSelectedBook() {
        _selectedBook.value?.let { book ->
            val params = ReserveBookParams(
                bookId = book.id,
                meetingTime = null,
                comment = "Хочу обменяться!"
            )
            viewModelScope.launch {
                reserveBookUseCase(params).collect { result ->
                    if (result is Resource.Success) {
                        _interactionEvent.value = InteractionEvent.ShowToast("Запрос на обмен отправлен!")
                        // Закрываем BottomSheet после успешного резерва
                        onBottomSheetDismissed()
                    } else if (result is Resource.Error) {
                        _interactionEvent.value = InteractionEvent.ShowToast("Ошибка резервирования: ${result.message}")
                    }
                }
            }
        }
    }

    /**
     * Сбрасывает событие взаимодействия после его обработки.
     */
    fun onInteractionEventConsumed() {
        _interactionEvent.value = null
    }
}

/**
 * Класс для представления событий взаимодействия с UI.
 */
sealed class InteractionEvent {
    data class ShowToast(val message: String) : InteractionEvent()
}
