package com.example.gengolearning.ui.features.dashboard.home.mainlanguage

import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages

sealed interface LanguageSelectorUiEvents {
    data object NavigateToDashboard: LanguageSelectorUiEvents
}

data class LanguageSelectorUiState(
    val isLoading: Boolean = false,
    val selectedMainLanguage: String = "",
    val selectedLanguage: String = "",
    val languages:List<Language> = Languages.languagesList,
    val currentLanguage: Language = Languages.languagesList.first(),
    val mainLanguage: String = ""
)