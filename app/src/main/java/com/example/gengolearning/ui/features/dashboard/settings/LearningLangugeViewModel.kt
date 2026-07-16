package com.example.gengolearning.ui.features.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.ui.features.dashboard.home.mainlanguage.LanguageSelectorUiEvents
import com.example.gengolearning.ui.features.dashboard.home.mainlanguage.LanguageSelectorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LearningLanguageViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageWords,
    private val grammarRepo: LanguageGrammar
): ViewModel() {


   private val currentLanguage = userSettingsRepository.selectedLanguage
    private val mainLanguage = userSettingsRepository.getMainLanguage()

    private val _uiEvents = Channel<LanguageSelectorUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(LanguageSelectorUiState())
    val uiState = combine(_uiState, mainLanguage, currentLanguage){
        state, mainLanguage, currentLanguage ->
        state.copy(
            mainLanguage = mainLanguage,
            currentLanguage = currentLanguage,
            selectedLanguage = currentLanguage.code,
            selectedMainLanguage = mainLanguage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LanguageSelectorUiState()
    )

    fun selectCurrentLanguage(language: String) {
        _uiState.update {
            it.copy(
                selectedLanguage = language
            )
        }
    }

    fun selectMainLanguage(language: String) {
        _uiState.update {
            it.copy(
                selectedMainLanguage = language
            )
        }
    }

    fun setLanguage(selectedLanguage: String) {

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            coroutineScope {
               userSettingsRepository.setLanguage(selectedLanguage)

                launch {
                    repository.loadWords(selectedLanguage)
                    repository.loadCategories(selectedLanguage)
                }

                launch {  grammarRepo.loadGrammar(selectedLanguage) }

                println("Selected language: $selectedLanguage")


            }

            _uiState.update { it.copy(
                isLoading = false
            ) }
            _uiEvents.send(LanguageSelectorUiEvents.NavigateToDashboard)
        }
    }

    fun setMainLanguage(selectedLanguage: String){
        viewModelScope.launch {
                userSettingsRepository.setMainLanguage(selectedLanguage)
            setLanguage(selectedLanguage)
        }

    }


}