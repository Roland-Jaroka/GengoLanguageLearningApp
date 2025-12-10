package com.example.gengolearning.uielements.dashboard.home.mylist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.model.results.AddWordResults
import com.example.gengolearning.words.LanguageWords
import com.gengolearning.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
sealed class EditWordState{
    object Success: EditWordState()
    object Normal: EditWordState()
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

    private val _editWordState = MutableStateFlow<EditWordState>(EditWordState.Normal)
    val editWordState = _editWordState.asStateFlow()

    var wordInputError by mutableStateOf<Int?>(null)
        private set
    var translationInputError by mutableStateOf<Int?>(null)
        private set




    init {
        repository.words.onEach { list->
            val exactWord = list.find { it.id == wordId }
            _word.value = exactWord

        }.launchIn(viewModelScope)
    }

    fun onWordInputChange(newWord: String) {
        _word.value = _word.value?.copy(word = newWord)
        wordInputError = null
    }

    fun onPronunciationInputChange(newWord: String) {
      _word.value = _word.value?.copy(pronunciation = newWord)
    }
    fun onTranslationInputChange(newWord: String) {
       _word.value = _word.value?.copy(translation = newWord)
        translationInputError = null
    }


    fun fieldValidation(word: String, translation: String): AddWordResults {
        if (word.isBlank()) return AddWordResults.BlankWord
        if (translation.isBlank()) return AddWordResults.BlankTranslation
        return AddWordResults.Success
    }

    fun onUpdate(currentLanguage: String) {

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
                   _editWordState.value = EditWordState.Success
               }

               }
           }

       }

}