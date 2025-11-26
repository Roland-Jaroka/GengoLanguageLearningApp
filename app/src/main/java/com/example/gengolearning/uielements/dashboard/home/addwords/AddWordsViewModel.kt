package com.example.gengolearning.uielements.dashboard.home.addwords

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.Tonemarks.toPinyin
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.model.results.AddWordResults
import com.example.gengolearning.words.LanguageWords
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth

class AddWordsViewModel(
): ViewModel() {


    val auth= FirebaseAuth.getInstance()
    var word by mutableStateOf("")
        private set

    val currentLanguage= UserSettingsRepository.language.value

    var translation by mutableStateOf("")
        private set

    var pronunciation by mutableStateOf("")
        private set

    var wordInputError by mutableStateOf<Int?>(null)
        private set
    var translationInputError by mutableStateOf<Int?>(null)
        private set

    var error by mutableStateOf<String?>(null)
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

    fun fieldValidation(word: String, translation: String): AddWordResults {
       if (word.isBlank()) return AddWordResults.BlankWord
       if (translation.isBlank()) return AddWordResults.BlankTranslation
        return AddWordResults.Success

    }
    fun addWordToList() {

        val result = fieldValidation(word, translation)
        when (result) {
            is AddWordResults.BlankWord -> wordInputError = R.string.word_input_error
            is AddWordResults.BlankTranslation -> translationInputError = R.string.translation_input_error
            is AddWordResults.Success -> {
                try {

                    val newWord = Words(word, pronunciation, translation, id= java.util.UUID.randomUUID().toString())

                    LanguageWords.addWord(Words(word, pronunciation, translation, id = ""), currentLanguage)

                    word=""
                    pronunciation=""
                    translation=""
                   error = null
                } catch (e: Exception) {
                         error = e.message ?: "Unknown error"
                    }
            }


        }


    }




}