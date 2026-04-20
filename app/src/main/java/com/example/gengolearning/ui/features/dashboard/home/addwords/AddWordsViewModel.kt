package com.example.gengolearning.ui.features.dashboard.home.addwords

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.utils.Tonemarks.toPinyin
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.results.AddWordResults
import com.example.gengolearning.data.repositories.LanguageWords
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class AddWordsViewModel @Inject constructor(
    private val repository: LanguageWords,
    private val userSettingsRepository: UserSettingsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {


    val auth= FirebaseAuth.getInstance()
   private val getWord: String = savedStateHandle["word"]?: ""
  private  val getPronunciation: String = savedStateHandle["pronunciation"]?: ""
    private val getTranslation: String = savedStateHandle["translation"]?: ""

    var word by mutableStateOf(getWord)
        private set

    val currentLanguage= userSettingsRepository.language.value

    var translation by mutableStateOf(getTranslation)
        private set

    var pronunciation by mutableStateOf(getPronunciation)
        private set

    var wordInputError by mutableStateOf<Int?>(null)
        private set
    var translationInputError by mutableStateOf<Int?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var isOnHomePage by mutableStateOf(true)
        private set

    var showWordInLibraryDialog by mutableStateOf(false)
        private set

    var existingWordInLibrary by mutableStateOf<Words?>(null)
        private set



    fun onTranslationChange(newTranslation: String) {
        translation = newTranslation
        translationInputError = null
    }

    fun onWordChange(newWord: String) {
        word = newWord
        wordInputError = null
    }


    fun onPronunciationChange(newPronunciation: String) {
        pronunciation = toPinyin(newPronunciation)

    }

    fun setIsOnHomePage() {
        isOnHomePage = !isOnHomePage
    }

    fun onDismissDialog() {
        showWordInLibraryDialog = false
    }


  private suspend fun fieldValidation(word: String, translation: String): AddWordResults {
      val wordList = repository.words.first()
      val existingWord = wordList.find { it.word == word }

       if (word.isBlank()) return AddWordResults.BlankWord
       if (translation.isBlank()) return AddWordResults.BlankTranslation
       if (wordList.any{it.word == word}) {

           existingWordInLibrary = existingWord

           return AddWordResults.WordAlreadyExits }

        return AddWordResults.Success

    }
    fun addWordToList() {
        viewModelScope.launch {
        val result = fieldValidation(word, translation)
        when (result) {
            is AddWordResults.BlankWord -> wordInputError = R.string.word_input_error
            is AddWordResults.BlankTranslation -> translationInputError = R.string.translation_input_error
            is AddWordResults.WordAlreadyExits -> showWordInLibraryDialog = true
            is AddWordResults.Success -> {

                       addWordToListAndFirebase()
            }

            }


        }


    }

    fun addWordToListAndFirebase() {
      viewModelScope.launch {
          try {
              val newWord = Words(
                  word,
                  pronunciation,
                  translation,
                  id = UUID.randomUUID().toString(),
                  language = currentLanguage,
                  isOnHomePage = isOnHomePage
              )

              repository.addWord(newWord, currentLanguage)

              word = ""
              pronunciation = ""
              translation = ""
              error = null

          } catch (e: Exception) {
              error = e.message ?: "Unknown error"
          }
      }
    }



}