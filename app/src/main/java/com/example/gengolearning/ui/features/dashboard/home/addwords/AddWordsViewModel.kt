package com.example.gengolearning.ui.features.dashboard.home.addwords

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.results.AddWordResults
import com.example.gengolearning.model.utils.Tonemarks.toPinyin
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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


    val currentLanguage= userSettingsRepository.language.value


      private val _uiState = MutableStateFlow(AddWordsUiState(
          word = getWord, translation = getTranslation, pronunciation = getPronunciation
      ))
      val state = _uiState.asStateFlow()

    private val _events = Channel<AddWordsEvents>()
    val events = _events.receiveAsFlow()


    fun onAction(action: AddWordsActions) {
        when(action) {
           is AddWordsActions.OnAddWordToList -> addWordToList(
               word = action.word, pronunciation = action.pronunciation, translation = action.translation,
               isOnHomePage = action.isOnHomePage
           )
            is  AddWordsActions.OnAddWordsToListAndFirebase -> addWordToListAndFirebase(
                word = action.word, pronunciation = action.pronunciation, translation = action.translation,
                isOnHomePage = action.isOnHomePage
            )
            AddWordsActions.OnDismissDialog -> onDismissDialog()
            is AddWordsActions.OnPronunciationChange -> onPronunciationChange(action.newPronunciation)
            is AddWordsActions.OnTranslationChange -> onTranslationChange(action.newTranslation)
            is AddWordsActions.OnWordChange -> onWordChange(action.newWord)
            AddWordsActions.SetIsOnHomepage -> setIsOnHomePage()
        }
    }


  private  fun onTranslationChange(newTranslation: String) {

        _uiState.update { it.copy(
            translation = newTranslation,
            translationInputError = null
        ) }
    }

   private fun onWordChange(newWord: String) {
        _uiState.update {
            it.copy(
                word = newWord,
                wordInputError = null
            )
        }
    }


    private fun onPronunciationChange(newPronunciation: String) {
        _uiState.update {
            it.copy(
                pronunciation = toPinyin(newPronunciation)
            )
        }

    }

   private fun setIsOnHomePage() {

        _uiState.update {
            it.copy(
                isOnHomePage = !it.isOnHomePage
            )
        }
    }

  private  fun onDismissDialog() {

        _uiState.update {
            it.copy(
                showWordInLibraryDialog = false
            )
        }
    }


  private suspend fun fieldValidation(word: String, translation: String): AddWordResults {
      val wordList = repository.words.first()
      val existingWord = wordList.find { it.word == word }

       if (word.isBlank()) return AddWordResults.BlankWord
       if (translation.isBlank()) return AddWordResults.BlankTranslation
       if (wordList.any{it.word == word}) {

           _uiState.update { it.copy(
               existingWordInLibrary = existingWord
           ) }

           return AddWordResults.WordAlreadyExits }

        return AddWordResults.Success

    }
   private fun addWordToList(word: String,
                             translation: String,
                             pronunciation: String, isOnHomePage: Boolean) {
        viewModelScope.launch {


        val result = fieldValidation(word, translation)
        when (result) {
            is AddWordResults.BlankWord -> _uiState.update {
                it.copy(wordInputError = R.string.word_input_error)
            }
            is AddWordResults.BlankTranslation ->
            _uiState.update {
                it.copy(
                    translationInputError = R.string.translation_input_error
                )
            }
            is AddWordResults.WordAlreadyExits ->
            _uiState.update {
                it.copy(
                    showWordInLibraryDialog = true
                )
            }
            is AddWordResults.Success -> {

                       addWordToListAndFirebase(
                           word, pronunciation, translation, isOnHomePage
                       )

                       _events.send(AddWordsEvents.showSnackBar)
            }

            }


        }


    }

 private fun addWordToListAndFirebase(word: String,
                                        pronunciation: String,
                                        translation: String,
                                        isOnHomePage: Boolean) {
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

              _uiState.update {
                  it.copy(
                      word = "",
                      pronunciation = "",
                      translation = "",
                      error = null
                  )
              }

          } catch (e: Exception) {
              _uiState.update {
                  it.copy(
                      error = e.message ?: "Unkown error"
                  )
              }
          }
      }
    }



}