package com.example.mylanguagelearningapp.uielements.dashboard.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.words.ChineseWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {


    var wordsList = mutableStateListOf<Words>()
    var currentIndex = mutableStateOf(0)


    var currentWord = mutableStateOf<Words?>(null)


    init {

        snapshotFlow { UserSettingsRepository.language.value }
            .onEach { updateWordsList(it) }
            .launchIn(viewModelScope)

        if (wordsList.isNotEmpty()) {

            currentWord.value = wordsList[0]
        }

        snapshotFlow { wordsList.size }
            .filter { it > 0 }
            .onEach {

                val sorted = wordsList.sortedBy { it.word }
                wordsList.clear()
                wordsList.addAll(sorted)

                currentWord.value = wordsList[0]
                currentIndex.value = 0
            }
            .launchIn(viewModelScope)

    }

    fun updateWordsList(currentLanguage: String) {
        if (currentLanguage == "jp") {
            wordsList = JapaneseWords.wordList
        } else if (currentLanguage == "cn") {

            wordsList = ChineseWords.chinseWordsList
        }
    }

    fun loadData() {

        JapaneseWords.loadWords()
        JapaneseGrammar.loadGrammar()
        ChineseWords.loadWords()



    }


    fun onNextClick() {

        if (wordsList.isEmpty()) return

        currentIndex.value = (currentIndex.value + 1) % wordsList.size

        currentWord.value = wordsList[currentIndex.value]
    }

    fun onPreviousClick() {
        if (wordsList.isEmpty()) return
        currentIndex.value = (currentIndex.value - 1 + wordsList.size) % wordsList.size
        currentWord.value = wordsList[currentIndex.value]
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
}

