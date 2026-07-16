package com.example.gengolearning.ui.features.dashboard.home.addwords

import com.example.gengolearning.model.appmodels.Words

 data class AddWordsUiState(
    val word: String = "",
    val translation: String = "",
    val pronunciation: String = "",
    val wordInputError: Int? = null,
    val translationInputError: Int? = null,
    val error: String? = null,
    val isOnHomePage: Boolean = true,
    val showWordInLibraryDialog: Boolean = false,
    val existingWordInLibrary: Words? = null
)
sealed interface AddWordsEvents {
    data object showSnackBar: AddWordsEvents
}

sealed interface AddWordsActions {
   data class OnWordChange(val newWord: String): AddWordsActions
   data class OnTranslationChange(val newTranslation: String): AddWordsActions
   data class OnPronunciationChange(val newPronunciation: String): AddWordsActions
   data object SetIsOnHomepage: AddWordsActions
   data object OnDismissDialog: AddWordsActions
   data class OnAddWordToList(val word: String,
                              val pronunciation: String,
                              val translation: String,
                              val isOnHomePage: Boolean): AddWordsActions
   data class OnAddWordsToListAndFirebase(val word: String,
                                          val pronunciation: String,
                                          val translation: String,
                                          val isOnHomePage: Boolean): AddWordsActions

}