package com.example.gengolearning.ui.features.dashboard.home.apiwords

import android.content.Context
import com.example.gengolearning.model.appmodels.ErrorModalText
import com.example.gengolearning.model.appmodels.Words

data class ApiWordsUiState(
    val isLoading: Boolean = false,
    val wordList: List<Words> = emptyList(),
    val error: Boolean? = null,
    val modalText: ErrorModalText? = null,
    val searchInput : String = "",
    val tutorialWithInfo: Boolean = false
)


sealed interface ApiWordsActions{
    data class OnSearchInput(val input: String): ApiWordsActions
    data class OnLoadWords(val searchKey: String): ApiWordsActions
    data class OnSetTutorial(val context: Context): ApiWordsActions
    data object OnResetError: ApiWordsActions

    data class OnSetTutorialWithInfo(val visible: Boolean): ApiWordsActions
}