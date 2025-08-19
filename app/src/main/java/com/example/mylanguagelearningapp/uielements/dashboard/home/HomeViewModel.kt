package com.example.mylanguagelearningapp.uielements.dashboard.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.japanesewords.JapaneseWords
import com.example.mylanguagelearningapp.model.Words
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel: ViewModel() {

    val wordsList = JapaneseWords.wordList

    var currentIndex = mutableStateOf(0)


    var currentWord= mutableStateOf<Words?>(null)

    init {
        if (wordsList.isNotEmpty()) {

            currentWord.value= wordsList[0]
        }
        snapshotFlow { wordsList.size }
            .filter { it > 0 }
            .onEach {

                val sorted= wordsList.sortedBy { it.word }
                wordsList.clear()
                wordsList.addAll(sorted)

                currentWord.value= wordsList[0]
                currentIndex.value= 0
            }
            .launchIn(viewModelScope)

    }


    fun onNextClick(){

        if (wordsList.isEmpty()) return

        currentIndex.value= (currentIndex.value + 1) % wordsList.size

         currentWord.value= wordsList[currentIndex.value]
    }

    fun onPreviousClick(){
        if (wordsList.isEmpty()) return
        currentIndex.value= (currentIndex.value - 1 + wordsList.size) % wordsList.size
        currentWord.value= wordsList[currentIndex.value]
    }

    var isWordVisible by mutableStateOf(true)
    var isPronunciationVisible by mutableStateOf(true)
    var isTranslationVisible by mutableStateOf(true)

    fun onWordClick() {
        isWordVisible= !isWordVisible
    }

    fun onPronunciationClick() {
        isPronunciationVisible= !isPronunciationVisible
    }

    fun onTranslationClick() {

        isTranslationVisible= !isTranslationVisible
    }
}

