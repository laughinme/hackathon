package com.example.hackathon.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.data.remote.dto.UserLoginRequest
import com.example.hackathon.data.remote.dto.UserRegisterRequest
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.TokenPair
import com.example.hackathon.domain.usecase.LoginUseCase
import com.example.hackathon.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

const val ONBOARDING_VIEWMODEL_TAG = "OnboardingViewModel"

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    //=========================================
    //       Всё для sign in / sign up
    //=========================================
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _authState = MutableStateFlow<Resource<TokenPair>?>(null)
    val authState = _authState.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onSignInClicked() {
        viewModelScope.launch {
            loginUseCase(UserLoginRequest(email.value, password.value))
                .collect { result ->
                    _authState.value = result
                }
        }
    }

    fun onSignUpClicked() {
        viewModelScope.launch {
            // Предполагаем, что у тебя есть поле для username
            val username = "some_username" // Возьми из соответствующего StateFlow
            registerUseCase(UserRegisterRequest(email.value, password.value, username))
                .collect { result ->
                    _authState.value = result
                }
        }
    }


    //=========================================
    //           Всё для AgePicker
    //=========================================

    private val _pickedDate = MutableStateFlow("")

    fun onPickedDate(date: LocalDate) {
        _pickedDate.value = date.toString()
    }

    fun onAgePickerClicked() {
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Выбрана дата рождения: $_pickedDate")
    }

    //=========================================
    //         Всё для GenderPicker
    //=========================================

    private val _selectedGender = MutableStateFlow("")
    val selectedGender = _selectedGender.asStateFlow()

    fun onSelectedGenderChanged(newGender: String) {
        _selectedGender.value = newGender
    }

    fun onGenderPickerClicked() {
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Выбран гендер: $_selectedGender")
    }

    //=========================================
    //             Всё для City
    //=========================================

    private val _expanded = MutableStateFlow(false)
    val expanded = _expanded.asStateFlow()

    private val _selectedCity = MutableStateFlow("")
    val selectedCity = _selectedCity.asStateFlow()

    fun onExpandedChanged() {
        _expanded.value = !_expanded.value
    }

    fun onCitySelected(city: String) {
        _selectedCity.value = city
    }

    fun onCityClicked() {
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Выбран город: $_selectedCity")
    }

    //=========================================
    //          Всё для genresPicker
    //=========================================

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
        Log.d(ONBOARDING_VIEWMODEL_TAG, "Выбранные жанры: $_selectedGenres")
    }
}