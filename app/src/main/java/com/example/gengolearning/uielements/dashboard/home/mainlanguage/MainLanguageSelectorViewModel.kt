package com.example.gengolearning.uielements.dashboard.home.mainlanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainLanguageSelectorViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    fun setMainLanguage(language: String) {
        viewModelScope.launch {
        userSettingsRepository.setMainLanguage(language)
    }

    }

    fun setLanguage(selectedLanguage: String) {
        viewModelScope.launch {
            userSettingsRepository.setLanguage(selectedLanguage)
        }
    }

}