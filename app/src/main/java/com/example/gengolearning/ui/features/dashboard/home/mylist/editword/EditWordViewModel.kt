package com.example.gengolearning.ui.features.dashboard.home.mylist.editword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.results.AddWordResults
import com.gengolearning.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class EditWordEvents{
    object Navigate: EditWordEvents()
}
@HiltViewModel
class EditWordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LanguageWords,
    userSettingsRepository: UserSettingsRepository
): ViewModel() {

   private val wordId: String = savedStateHandle["wordId"]!!

    val currentLanguage = userSettingsRepository.selectedLanguage



    private val _word = MutableStateFlow<Words?>(null)
    val word = _word.asStateFlow()

   private val _events = MutableSharedFlow<EditWordEvents>()
    val events = _events.asSharedFlow()


    val categories = combine(
        repository.categories,
        _word
    ) {
            categories, word ->
        categories.filter { category ->
            word?.category?.contains(category.categoryName) == true
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _addedCategories = MutableStateFlow<List<WordCategories>>(emptyList())
    val addedCategories = _addedCategories.asStateFlow()

    private val _deleteableCategories = MutableStateFlow<List<WordCategories>>(emptyList())
    val deleteableCategories = _deleteableCategories.asStateFlow()



    var wordInputError by mutableStateOf<Int?>(null)
        private set
    var translationInputError by mutableStateOf<Int?>(null)
        private set




    init {
        repository.words.onEach { list->
            val exactWord = list.find { it.id == wordId }
            _word.value = exactWord

        }.launchIn(viewModelScope)

        viewModelScope.launch {

            categories.collect { categories ->
                _addedCategories.value = categories
            }
        }

    }

    fun onAction(action: EditWordActions) {
        when (action) {
            is EditWordActions.OnCategoryClick -> onCategoryClick(action.categories)
            is EditWordActions.OnDeletableCategoryClick -> onDeleteableCategoryClick(action.categories)
            is EditWordActions.OnPronunciationInputChange -> onPronunciationInputChange(action.newInput)
            is EditWordActions.OnTranslationInputChange -> onTranslationInputChange(action.newInput)
            is EditWordActions.OnUpdate -> onUpdate(action.currentLanguage)
            is EditWordActions.OnWordInputChange -> onWordInputChange(action.newInput)
        }
    }

    private fun onCategoryClick(categories: WordCategories) {

        _deleteableCategories.update {
            it + categories
        }

        _addedCategories.update {
            it - categories
        }
    }

    private fun onDeleteableCategoryClick(categories: WordCategories) {
        _addedCategories.update {
            it + categories
        }
        _deleteableCategories.update {
            it - categories
        }
    }

  private  fun onWordInputChange(newWord: String) {
        _word.value = _word.value?.copy(word = newWord)
        wordInputError = null
    }

 private   fun onPronunciationInputChange(newWord: String) {
      _word.value = _word.value?.copy(pronunciation = newWord)
    }
 private   fun onTranslationInputChange(newWord: String) {
       _word.value = _word.value?.copy(translation = newWord)
        translationInputError = null
    }


   private fun fieldValidation(word: String, translation: String): AddWordResults {
        if (word.isBlank()) return AddWordResults.BlankWord
        if (translation.isBlank()) return AddWordResults.BlankTranslation
        return AddWordResults.Success
    }

  private  fun onUpdate(currentLanguage: String) {

        val currentWord = _word.value ?: return

        val result = fieldValidation(currentWord.word, currentWord.translation)

       when (result) {
           is AddWordResults.BlankWord -> wordInputError = R.string.word_input_error
           is AddWordResults.BlankTranslation -> translationInputError = R.string.translation_input_error
           is AddWordResults.Success -> {


               viewModelScope.launch {

                   repository.updateWord(
                       currentWord.id,
                       currentWord.word,
                       currentWord.translation,
                       currentWord.pronunciation,
                       currentLanguage,
                       currentWord
                   )

                   if (_deleteableCategories.value.isNotEmpty()) {
                       _deleteableCategories.value.forEach { category ->
                           repository.updateLocalWord(
                               currentWord.copy(
                                   category = currentWord.category - category.categoryName
                               )
                           )

                           //Update word with category on firebase
                           repository.updateWordWithCategoryOnFirebase(currentWord.copy(
                               category = currentWord.category - category.categoryName
                           ), language = currentLanguage
                           )
                       }
                   }
                   _events.emit(EditWordEvents.Navigate)
               }

               }

           AddWordResults.WordAlreadyExits -> {}
       }

       }

}