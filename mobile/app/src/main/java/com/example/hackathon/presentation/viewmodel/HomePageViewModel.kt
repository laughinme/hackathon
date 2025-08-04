package com.example.hackathon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.usecase.GetBooksForYouUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val getBooksForYouUseCase: GetBooksForYouUseCase
) : ViewModel() {

    private val _booksForYouState = MutableStateFlow<Resource<List<Book>>>(Resource.Loading())
    val booksForYouState: StateFlow<Resource<List<Book>>> = _booksForYouState.asStateFlow()

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
}
