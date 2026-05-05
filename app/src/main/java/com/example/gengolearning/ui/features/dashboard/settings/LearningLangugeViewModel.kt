package com.example.gengolearning.ui.features.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.data.repositories.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LearningLanguageViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageWords,
    private val grammarRepo: LanguageGrammar
): ViewModel() {


    val currentLanguage: StateFlow<Language> = userSettingsRepository.selectedLanguage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Languages.languagesList[0]
    )
    val mainLanguage = userSettingsRepository.getMainLanguage()


    fun setLanguage(selectedLanguage: String) {

        viewModelScope.launch {
            userSettingsRepository.setLanguage(selectedLanguage)
            repository.loadWords(selectedLanguage)
            repository.loadCategories(selectedLanguage)
            grammarRepo.loadGrammar(selectedLanguage)

            println("Selected language: $selectedLanguage")
        }
    }

    fun setMainLanguage(selectedLanguage: String){
        viewModelScope.launch {
                userSettingsRepository.setMainLanguage(selectedLanguage)
            setLanguage(selectedLanguage)
        }

    }


}