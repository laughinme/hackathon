package com.example.hackathon.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.model.UserProfileUpdate
import com.example.hackathon.domain.repository.UserRepository
import com.example.hackathon.domain.usecase.GetCitiesUseCase
import com.example.hackathon.domain.usecase.GetGenresUseCase
import com.example.hackathon.domain.usecase.LogoutUseCase
import com.example.hackathon.domain.usecase.UpdateProfilePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

const val PROFILE_VIEWMODEL_TAG = "ProfileViewModel"


// --- СОБЫТИЯ ЭКРАНА ---
sealed interface ProfileEvent {
    data class OnUsernameChange(val value: String) : ProfileEvent
    data class OnBioChange(val value: String) : ProfileEvent
    data class OnBirthDateChange(val date: LocalDate) : ProfileEvent
    data class OnGenderChange(val gender: String) : ProfileEvent
    data class OnCityChange(val city: City) : ProfileEvent
    data class OnLanguageChange(val language: String) : ProfileEvent
    data class OnIsPublicChange(val isPublic: Boolean) : ProfileEvent
    data class OnGenreChange(val genre: Genre) : ProfileEvent
    data class OnPictureSelected(val file: File) : ProfileEvent
    data class OnLocationUpdate(val lat: Double, val lon: Double) : ProfileEvent
    object OnSaveClick : ProfileEvent
    object OnLogoutClick : ProfileEvent
    object OnRetry : ProfileEvent
}


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val userRepository: UserRepository,
    private val getGenresUseCase: GetGenresUseCase,
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    // --- ОБЩЕЕ СОСТОЯНИЕ ЭКРАНА ---
    private val _profileState = MutableStateFlow<Resource<UserProfile>>(Resource.Loading())
    val profileState = _profileState.asStateFlow()

    // --- СОСТОЯНИЕ ДЛЯ ВЫХОДА ---
    private val _logoutState = MutableStateFlow<Resource<Unit>?>(null)
    val logoutState = _logoutState.asStateFlow()

    // --- СОСТОЯНИЕ ДЛЯ ЗАВЕРШЕНИЯ ОНБОРДИНГА ---
    private val _onboardingSaveComplete = MutableStateFlow<Boolean>(false)
    val onboardingSaveComplete = _onboardingSaveComplete.asStateFlow()

    // --- СОСТОЯНИЯ ДЛЯ ПОЛЕЙ ПРОФИЛЯ ---
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    private val _bio = MutableStateFlow("")
    val bio = _bio.asStateFlow()
    private val _birthDate = MutableStateFlow<LocalDate?>(null)
    val birthDate = _birthDate.asStateFlow()
    private val _gender = MutableStateFlow("")
    val gender = _gender.asStateFlow()
    private val _language = MutableStateFlow("")
    val language = _language.asStateFlow()
    private val _isPublic = MutableStateFlow(true)
    val isPublic = _isPublic.asStateFlow()
    private val _selectedCityId = MutableStateFlow<Int?>(null)
    val selectedCityId = _selectedCityId.asStateFlow()
    private val _selectedGenres = MutableStateFlow<Set<Genre>>(emptySet())
    val selectedGenres = _selectedGenres.asStateFlow()
    private val _avatarUrl = MutableStateFlow<String?>(null)
    val avatarUrl = _avatarUrl.asStateFlow()
    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude = _latitude.asStateFlow()
    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude = _longitude.asStateFlow()


    // --- СОСТОЯНИЯ ДЛЯ СПИСКОВ ДАННЫХ ---
    private val _allGenresState = MutableStateFlow<Resource<List<Genre>>>(Resource.Loading())
    val allGenresState = _allGenresState.asStateFlow()
    private val _allCitiesState = MutableStateFlow<Resource<List<City>>>(Resource.Loading())
    val allCitiesState = _allCitiesState.asStateFlow()

    init {
        loadProfile()
        loadAllGenres()
        loadAllCities() // Вызываем загрузку городов
    }

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.OnUsernameChange -> _username.value = event.value
            is ProfileEvent.OnBioChange -> _bio.value = event.value
            is ProfileEvent.OnBirthDateChange -> _birthDate.value = event.date
            is ProfileEvent.OnGenderChange -> _gender.value = event.gender
            is ProfileEvent.OnCityChange -> _selectedCityId.value = event.city.id
            is ProfileEvent.OnLanguageChange -> _language.value = event.language
            is ProfileEvent.OnIsPublicChange -> _isPublic.value = event.isPublic
            is ProfileEvent.OnGenreChange -> onGenreSelected(event.genre)
            is ProfileEvent.OnPictureSelected -> uploadProfilePicture(event.file)
            is ProfileEvent.OnLocationUpdate -> {
                _latitude.value = event.lat
                _longitude.value = event.lon
            }
            ProfileEvent.OnSaveClick -> saveProfile()
            ProfileEvent.OnLogoutClick -> logout()
            ProfileEvent.OnRetry -> {
                loadProfile()
                loadAllGenres()
                loadAllCities() // Добавляем перезагрузку городов
            }
        }
    }

    private fun loadProfile() {
        userRepository.getProfile().onEach { result ->
            if (result is Resource.Success) {
                result.data?.let { profile ->
                    _username.value = profile.username ?: ""
                    _bio.value = profile.bio ?: ""
                    _selectedGenres.value = profile.favoriteGenres.toSet()
                    _selectedCityId.value = profile.city?.id
                    _gender.value = profile.gender ?: ""
                    _language.value = profile.language ?: ""
                    _isPublic.value = profile.isPublic
                    profile.birthDate?.let {
                        // Добавим проверку на пустую строку, чтобы избежать крэша
                        if (it.isNotBlank()) {
                            _birthDate.value = LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
                        }
                    }
                    _avatarUrl.value = profile.avatarUrl
                    _latitude.value = profile.latitude
                    _longitude.value = profile.longitude
                }
            }
            _profileState.value = result
        }.launchIn(viewModelScope)
    }

    private fun loadAllGenres() {
        getGenresUseCase().onEach { result ->
            _allGenresState.value = result
        }.launchIn(viewModelScope)
    }

    // Новая функция для загрузки списка городов
    private fun loadAllCities() {
        getCitiesUseCase().onEach { result ->
            _allCitiesState.value = result
        }.launchIn(viewModelScope)
    }

    private fun onGenreSelected(genre: Genre) {
        val current = _selectedGenres.value.toMutableSet()
        if (genre in current) current.remove(genre) else current.add(genre)
        _selectedGenres.value = current
    }

    private fun saveProfile() {
        viewModelScope.launch {
            _profileState.value = Resource.Loading()

            val profileUpdateData = UserProfileUpdate(
                username = _username.value.takeIf { it.isNotBlank() },
                avatarUrl = _avatarUrl.value,
                bio = _bio.value.takeIf { it.isNotBlank() },
                birthDate = _birthDate.value,
                gender = _gender.value.takeIf { it.isNotBlank() },
                language = _language.value.takeIf { it.isNotBlank() },
                cityId = _selectedCityId.value,
                latitude = _latitude.value,
                longitude = _longitude.value,
                isPublic = _isPublic.value,
                favoriteGenreIds = _selectedGenres.value.map { it.id }
            )

            userRepository.updateFullProfile(profileUpdateData).onEach { result ->
                _profileState.value = result
                if (result is Resource.Success) {
                    Log.d(PROFILE_VIEWMODEL_TAG, "Profile and genres saved successfully!")
                    _onboardingSaveComplete.value = true
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun uploadProfilePicture(file: File) {
        updateProfilePictureUseCase(file).onEach { result ->
            // При успехе, сервер вернет обновленный профиль.
            // Мы можем обновить только URL аватара или весь стейт профиля.
            if (result is Resource.Success) {
                _avatarUrl.value = result.data?.avatarUrl
            }
            // Также можно обновить _profileState, чтобы показать индикатор загрузки/ошибку
            // _profileState.value = result
        }.launchIn(viewModelScope)
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect { result ->
                _logoutState.value = result
            }
        }
    }
}
