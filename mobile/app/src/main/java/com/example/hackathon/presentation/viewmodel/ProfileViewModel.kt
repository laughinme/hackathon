package com.example.hackathon.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon.data.remote.dto.UserPatchRequest
import com.example.hackathon.domain.model.Resource
import com.example.hackathon.domain.model.UserProfile
import com.example.hackathon.domain.usecase.GetProfileUseCase
import com.example.hackathon.domain.usecase.LogoutUseCase
import com.example.hackathon.domain.usecase.UpdateGenresUseCase
import com.example.hackathon.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    data class OnCityChange(val city: String) : ProfileEvent
    data class OnGenreSelected(val genre: String) : ProfileEvent
    object OnSaveProfileClick : ProfileEvent
    object OnSaveOnboardingClick : ProfileEvent
    object OnLogoutClick : ProfileEvent
    object OnRetry : ProfileEvent
}


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateGenresUseCase: UpdateGenresUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    // --- ОБЩЕЕ СОСТОЯНИЕ ЭКРАНА ---
    private val _profileState = MutableStateFlow<Resource<UserProfile>>(Resource.Loading())
    val profileState = _profileState.asStateFlow()

    // --- СОСТОЯНИЕ ДЛЯ ВЫХОДА ---
    private val _logoutState = MutableStateFlow<Resource<Unit>?>(null)
    val logoutState = _logoutState.asStateFlow()

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

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.OnUsernameChange -> _username.value = event.value
            is ProfileEvent.OnBioChange -> _bio.value = event.value
            is ProfileEvent.OnBirthDateChange -> _birthDate.value = event.date
            is ProfileEvent.OnGenderChange -> _gender.value = event.gender
            is ProfileEvent.OnCityChange -> _city.value = event.city
            is ProfileEvent.OnGenreSelected -> onGenreSelected(event.genre)
            ProfileEvent.OnSaveProfileClick -> saveProfile()
            ProfileEvent.OnSaveOnboardingClick -> saveOnboardingProfile()
            ProfileEvent.OnLogoutClick -> logout()
            ProfileEvent.OnRetry -> loadProfile()
        }
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

    private fun onGenreSelected(genre: String) {
        val current = _selectedGenres.value.toMutableSet()
        if (genre in current) current.remove(genre) else current.add(genre)
        _selectedGenres.value = current
    }

    private fun saveProfile() {
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

    private fun saveOnboardingProfile() {
        viewModelScope.launch {
            _profileState.value = Resource.Loading()
            val birthDateString = _birthDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val profileRequest = UserPatchRequest( /* ... */ )

            updateProfileUseCase(profileRequest).collect { profileResult ->
                when (profileResult) {
                    is Resource.Success -> {
                        val genreIds = listOf(1, 2, 3)
                        updateGenresUseCase(genreIds).collect { genreResult ->
                            if (genreResult is Resource.Success) {
                                _profileState.value = genreResult
                                _onboardingSaveComplete.value = true
                            } else {
                                _profileState.value = genreResult
                            }
                        }
                    }
                    is Resource.Error -> {
                        _profileState.value = profileResult
                    }
                    is Resource.Loading -> { /* Ignore */ }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect { result ->
                _logoutState.value = result
            }
        }
    }
}
