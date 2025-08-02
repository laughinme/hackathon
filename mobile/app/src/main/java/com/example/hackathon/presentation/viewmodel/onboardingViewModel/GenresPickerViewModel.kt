package com.example.hackathon.presentation.viewmodel.onboardingViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

const val GENRES_PICKER_VIEWMODEL_TAG = "GenresPickerViewModel"

class GenresPickerViewModel : ViewModel() {

    private val _allGenres = MutableStateFlow<List<String>>(emptyList())
    val allGenres = _allGenres.asStateFlow()

    private val _selectedGenres = MutableStateFlow<Set<String>>(emptySet())
    val selectedGenres = _selectedGenres.asStateFlow()

    fun onSelectedGenre(genre: String) {
        val currentSelected = selectedGenres.value.toMutableSet()
        if (genre in currentSelected) {
            currentSelected.remove(genre)
        } else {
            currentSelected.add(genre)
        }
        _selectedGenres.value = currentSelected
    }

    fun onGenrePickerClicked() {
        Log.d(GENRES_PICKER_VIEWMODEL_TAG, "Выбранные жанры: $_selectedGenres")
    }
}