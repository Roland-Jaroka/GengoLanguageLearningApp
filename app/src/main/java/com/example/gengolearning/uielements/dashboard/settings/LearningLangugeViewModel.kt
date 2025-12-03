package com.example.gengolearning.uielements.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.Language
import com.example.gengolearning.model.Languages
import com.example.gengolearning.model.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LearningLanguageViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {


    val currentLanguage: StateFlow<Language> = userSettingsRepository.selectedLanguage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Languages.languagesList[0]
    )
    val mainLanguage = userSettingsRepository.getMainLanguage()


    suspend fun setLanguage(selectedLanguage: String) {

        viewModelScope.launch {
            userSettingsRepository.setLanguage(selectedLanguage)
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