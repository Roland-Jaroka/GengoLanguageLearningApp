package com.example.gengolearning.ui.features.dashboard.home

import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.NewsResponse
import com.example.gengolearning.model.appmodels.Words

data class HomeUiState(
    val isLoading: Boolean = false,
    val wordList: List<Words> = emptyList(),
    val currentIndex: Int = 0,
    val currentWord: Words? = null,
    val currentLanguage: Language? = null,
    val news: List<NewsResponse> = emptyList(),
    val newsModal: Boolean = false,
    val quizIsEmptyModal: Boolean = false,
    val userName: String = "",
    val isWordVisible: Boolean = true,
    val isPronunciationVisible :Boolean = true,
    val isTranslationVisible: Boolean = true,
    val synchronized: Boolean = false,
    val isSyncing: Boolean = false,
    val syncedInfoModal: Boolean = false
)

sealed interface HomeActions
{
    data object OnNextClick: HomeActions
    data object OnPreviousClick: HomeActions
    data object OnWordClick: HomeActions
    data object OnPronounciationClick: HomeActions
    data object OnTranslationClick: HomeActions
    data object OnOpenNewsModal: HomeActions
    data object OnCloseNewsModal: HomeActions
    data object OnSyncWithCloud: HomeActions
    data object OnQuizClick: HomeActions
    data object OnDismissQuizEmptyModal: HomeActions

    data object OnGetData: HomeActions

    data object OnShowSyncedModal: HomeActions


}