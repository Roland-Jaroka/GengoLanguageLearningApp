package com.example.mylanguagelearningapp.uielements.dashboard.home.addwords

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextGranularity.Companion.Word
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.japanesewords.JapaneseWords
import com.example.mylanguagelearningapp.model.AddWordResults
import com.example.mylanguagelearningapp.model.Words
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AddWordsViewModel: ViewModel() {


    val auth= FirebaseAuth.getInstance()
    val uid= auth.currentUser?.uid.toString()
    var word by mutableStateOf("")
        private set

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
        pronunciation = newPronunciation

    }
    fun addWordToList(): AddWordResults {

        val error= JapaneseWords.error
        if (error.isNotBlank()) return AddWordResults.Error

        if (word.isBlank()) return AddWordResults.BlankWord
        if (translation.isBlank()) return AddWordResults.BlankTranslation

       try {

       JapaneseWords.addWord(Words(word, pronunciation, translation, id = ""))

           word=""
           pronunciation=""
           translation=""

       return AddWordResults.Success} catch (e:Exception) {
           return AddWordResults.Error

       }


    }



}