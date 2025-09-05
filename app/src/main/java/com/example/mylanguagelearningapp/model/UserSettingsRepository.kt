package com.example.mylanguagelearningapp.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSettingsRepository {

     val language = mutableStateOf("jp")
     val wordsList = mutableStateOf(JapaneseWords.wordList)
    //TODO Initiate a global List of Words based on language
    fun setLanguage(selectedLanguage: String) {
        language.value= selectedLanguage
        wordsList.value= when (selectedLanguage){
            "jp" -> JapaneseWords.wordList
            "cn" -> ChineseWords.chinseWordsList
            else -> JapaneseWords.wordList
        }
        println("Language set to: ${language.value}")
    }
}