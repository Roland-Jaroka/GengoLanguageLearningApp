package com.example.gengolearning.ui.features.dashboard.home.mainlanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.data.repositories.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState{
    object Idle: UiState()
    object Loading: UiState()
    object Success: UiState()
}
@HiltViewModel
class MainLanguageSelectorViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageWords,
    private val grammarRepo: LanguageGrammar
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState= _uiState.asStateFlow()

    fun setMainLanguage(language: String) {
        viewModelScope.launch {
        userSettingsRepository.setMainLanguage(language)
    }

    }

    fun setLanguage(selectedLanguage: String) {
        _uiState.value= UiState.Loading
        viewModelScope.launch {
            userSettingsRepository.setLanguage(selectedLanguage)
            repository.loadWords(selectedLanguage)
            grammarRepo.loadGrammar(selectedLanguage)

            _uiState.value= UiState.Success
        }

    }

    fun resetSate(){
        _uiState.value= UiState.Idle
    }

}