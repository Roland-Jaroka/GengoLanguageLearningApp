package com.example.mylanguagelearningapp.uielements.dashboard.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.words.LanguageWords
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    val repository = LanguageWords
    var wordsList = repository.words.map{ list->
        list.filter { it.isOnHomePage==true }.ifEmpty { list }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    var currentIndex = mutableStateOf(0)


    var currentWord = mutableStateOf<Words?>(null)




    init {
              repository.words.onEach{ list ->
                currentIndex.value = 0
                currentWord.value = list.firstOrNull()

            }.launchIn(viewModelScope)

    }

    fun loadData() {

        LanguageWords.loadWords(UserSettingsRepository.language.value)

        when (UserSettingsRepository.language.value){
            "jp"-> {
                JapaneseWords.loadWords()
                JapaneseGrammar.loadGrammar()
            }
            "cn" -> {
                ChineseWords.loadWords()
                ChineseGrammar.loadGrammar()
            }
        }



    }


    fun onNextClick() {
        val list = wordsList.value

        if (list.isEmpty()) return

        currentIndex.value = (currentIndex.value + 1) % list.size

        currentWord.value = list[currentIndex.value]
    }

    fun onPreviousClick() {

        val list = wordsList.value

        if (list.isEmpty()) return
        currentIndex.value = (currentIndex.value - 1 + list.size) % list.size
        currentWord.value = list[currentIndex.value]
    }

    var isWordVisible by mutableStateOf(true)
    var isPronunciationVisible by mutableStateOf(true)
    var isTranslationVisible by mutableStateOf(true)

    fun onWordClick() {
        isWordVisible = !isWordVisible
    }

    fun onPronunciationClick() {
        isPronunciationVisible = !isPronunciationVisible
    }

    fun onTranslationClick() {

        isTranslationVisible = !isTranslationVisible
    }
    fun migrateWords(language: String){
        when (language){
            "jp"-> {
                val oldList = JapaneseWords.wordList
                for (word in oldList){
                    LanguageWords.addWord(word, language)
                }
            }
            "cn" -> {
                val oldList = ChineseWords.chinseWordsList
                for (word in oldList){
                    LanguageWords.addWord(word, language)
                }
            }
        }
    }
}

