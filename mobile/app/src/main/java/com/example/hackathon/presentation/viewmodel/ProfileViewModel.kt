package com.example.hackathon.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.usecase.GetProfileUseCase
import com.example.hackathon.domain.usecase.UpdateGenresUseCase
import com.example.hackathon.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

const val PROFILE_VIEWMODEL_TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateGenresUseCase: UpdateGenresUseCase
) : ViewModel() {

    // --- ОБЩЕЕ СОСТОЯНИЕ ЭКРАНА ---
    private val _profileState = MutableStateFlow<Resource<UserProfile>>(Resource.Loading())
    val profileState = _profileState.asStateFlow()

    // --- СОСТОЯНИЯ ДЛЯ ПОЛЕЙ ---
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    private val _bio = MutableStateFlow("")
    val bio = _bio.asStateFlow()
    private val _birthDate = MutableStateFlow<LocalDate?>(null)
    val birthDate = _birthDate.asStateFlow()
    private val _gender = MutableStateFlow("")
    val gender = _gender.asStateFlow()
    private val _city = MutableStateFlow("")
    val city = _city.asStateFlow()
    private val _selectedGenres = MutableStateFlow<Set<String>>(emptySet())
    val selectedGenres = _selectedGenres.asStateFlow()

    val allGenres = MutableStateFlow(listOf("Fantasy", "Sci-Fi", "Mystery", "Thriller", "Romance", "Horror"))

    init {
        loadProfile()
    }

    fun loadProfile() {
        getProfileUseCase().onEach { result ->
            if (result is Resource.Success) {
                result.data?.let {
                    _username.value = it.username ?: ""
                    _bio.value = it.bio ?: ""
                }
            }
            _profileState.value = result
        }.launchIn(viewModelScope)
    }

    // --- ОБРАБОТЧИКИ СОБЫТИЙ С UI ---
    fun onUsernameChange(newUsername: String) { _username.value = newUsername }
    fun onBioChange(newBio: String) { _bio.value = newBio }
    fun onBirthDateChange(newDate: LocalDate) { _birthDate.value = newDate }
    fun onGenderChange(newGender: String) { _gender.value = newGender }
    fun onCityChange(newCity: String) { _city.value = newCity }
    fun onGenreSelected(genre: String) {
        val current = _selectedGenres.value.toMutableSet()
        if (genre in current) current.remove(genre) else current.add(genre)
        _selectedGenres.value = current
    }

    /**
     * Метод для сохранения изменений с экрана редактирования профиля.
     */
    fun onSaveProfileClicked() {
        viewModelScope.launch {
            val request = UserPatchRequest(
                username = username.value,
                bio = bio.value
            )
            updateProfileUseCase(request).onEach { result ->
                _profileState.value = result
                if (result is Resource.Success) {
                    Log.d(PROFILE_VIEWMODEL_TAG, "Profile updated successfully!")
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Метод для сохранения всех данных в конце онбординга.
     */
    fun saveOnboardingProfile() {
        Log.d(PROFILE_VIEWMODEL_TAG, "Attempting to save profile with data: " +
                "username=${_username.value}, " +
                "bio=${_bio.value}, " +
                "birthDate=${_birthDate.value}, " +
                "gender=${_gender.value}, " +
                "city=${_city.value}, " +
                "genres=${_selectedGenres.value}")
        viewModelScope.launch {
            val birthDateString = _birthDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val profileRequest = UserPatchRequest(
                username = _username.value.ifEmpty { null },
                bio = _bio.value.ifEmpty { null },
                birthDate = birthDateString,
                gender = _gender.value.lowercase().ifEmpty { null },
                cityId = if (_city.value.isNotEmpty()) 1 else null
            )
            updateProfileUseCase(profileRequest).onEach { profileResult ->
                if (profileResult is Resource.Success) {
                    val genreIds = listOf(1, 2, 3)
                    updateGenresUseCase(genreIds).onEach { genreResult ->
                        _profileState.value = genreResult
                    }.launchIn(viewModelScope)
                } else {
                    _profileState.value = profileResult
                }
            }.launchIn(viewModelScope)
        }
    }
}
