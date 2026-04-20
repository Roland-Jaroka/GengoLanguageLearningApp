package com.example.gengolearning.ui.features.dashboard.home.mainlanguage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.AppSettingsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState{
    object Idle: UiState()
    object Loading: UiState()
    object Success: UiState()
    object Error: UiState()
}
@HiltViewModel
class MainLanguageSelectorViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageWords,
    private val grammarRepo: LanguageGrammar,
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState= _uiState.asStateFlow()

    fun setMainLanguage(language: String) {
        viewModelScope.launch {
        userSettingsRepository.setMainLanguage(language)
    }

    }

    fun setLanguage(selectedLanguage: String, context: Context) {

        _uiState.value= UiState.Loading

        viewModelScope.launch {

            try {

                coroutineScope {

                    userSettingsRepository.setLanguage(selectedLanguage)

                  launch {

                      repository.loadCategories(selectedLanguage)

                      repository.loadWords(selectedLanguage, forceServerLoad = true)

                  }

                launch {  grammarRepo.loadGrammar(selectedLanguage) }

                }

                AppSettingsPreferences.setLoginDone(context, true)

                _uiState.value = UiState.Success

            } catch (e: Exception) {

                if (e is CancellationException) throw  e

                _uiState.value = UiState.Error
            }
        }

    }

    fun resetSate(){
        _uiState.value= UiState.Idle
    }

}