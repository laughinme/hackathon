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

    private val _onboardingSaveComplete = MutableStateFlow<Boolean>(false)
    val onboardingSaveComplete = _onboardingSaveComplete.asStateFlow()

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
        // ... твой лог
        viewModelScope.launch {
            _profileState.value = Resource.Loading() // Показываем индикатор загрузки на кнопке/экране

            val birthDateString = _birthDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val profileRequest = UserPatchRequest(
                // ... твои данные
            )

            // Сначала обновляем профиль
            updateProfileUseCase(profileRequest).collect { profileResult ->
                when (profileResult) {
                    is Resource.Success -> {
                        // Если профиль обновился, обновляем жанры
                        val genreIds = listOf(1, 2, 3) // <-- Тут пока заглушка, нужно будет сделать маппинг
                        updateGenresUseCase(genreIds).collect { genreResult ->
                            if (genreResult is Resource.Success) {
                                // ВСЁ ПРОШЛО УСПЕШНО!
                                _profileState.value = genreResult // Финальное успешное состояние
                                _onboardingSaveComplete.value = true // <-- СИГНАЛ К НАВИГАЦИИ!
                            } else {
                                // Ошибка при обновлении жанров
                                _profileState.value = genreResult
                            }
                        }
                    }
                    is Resource.Error -> {
                        // Ошибка при обновлении профиля
                        _profileState.value = profileResult
                    }
                    is Resource.Loading -> {
                        // Можно проигнорировать, т.к. мы уже в состоянии загрузки
                    }
                }
            }
        }
    }
}
