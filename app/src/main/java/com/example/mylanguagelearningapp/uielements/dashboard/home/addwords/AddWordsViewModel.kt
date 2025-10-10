package com.example.mylanguagelearningapp.uielements.dashboard.home.addwords

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.Tonemarks
import com.example.mylanguagelearningapp.model.Tonemarks.toPinyin
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.model.results.AddWordResults
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.words.ChineseWords
import com.google.firebase.auth.FirebaseAuth

class AddWordsViewModel: ViewModel() {


    val auth= FirebaseAuth.getInstance()
    var word by mutableStateOf("")
        private set
    val currentLanguage= UserSettingsRepository.language.value

    fun onWordChange(newWord: String) {
        word = newWord
    }

    var translation by mutableStateOf("")
        private set

    fun onTranslationChange(newTranslation: String) {
        translation = newTranslation
    }
    var pronunciation by mutableStateOf("")
        private set

    fun onPronunciationChange(newPronunciation: String) {
        pronunciation = toPinyin(newPronunciation)

    }
    fun addWordToList(): AddWordResults {

        val error= JapaneseWords.error
        if (error.isNotBlank()) return AddWordResults.Error

        if (word.isBlank()) return AddWordResults.BlankWord
        if (translation.isBlank()) return AddWordResults.BlankTranslation

       try {
          if (currentLanguage=="jp"){
       JapaneseWords.addWord(Words(word, pronunciation, translation, id = ""))}

           else if (currentLanguage=="cn"){
               ChineseWords.addWord(Words(word, pronunciation, translation, id = ""))
           }

           word=""
           pronunciation=""
           translation=""

       return AddWordResults.Success} catch (e:Exception) {
           return AddWordResults.Error

       }


    }




}