package com.example.mylanguagelearningapp.uielements.dashboard.home.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.model.QuizManager
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.model.QuizManager.quizzes
import com.example.mylanguagelearningapp.model.Tonemarks.toPinyin
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.model.quizWrongAnswers
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.LanguageWords
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class QuizViewModel: ViewModel() {

    val currentLanguage= UserSettingsRepository.language.value
    val repository= LanguageWords
    val wordsList = if (quizzes.isNotEmpty()) quizzes else repository.words.value


    var currentIndex by mutableStateOf(0)
        private set

    var currentWord = mutableStateOf<Words?>(null)

    var progress = mutableFloatStateOf(0f)

    var answer by mutableStateOf("")
        private set

    var points by mutableStateOf(0)
        private set

    var isQuizFinished by mutableStateOf(false)
        private set

    var wrongAnswers = mutableListOf<quizWrongAnswers>()
        private set


    fun onAnswerChange(newvalue: String) {
        answer = toPinyin(newvalue)
    }

    init {
        if (quizzes.isNotEmpty()){
            currentIndex = 0
            currentWord.value = quizzes[0]
            progress.floatValue = 0f
            answer= ""
        }
        else {
            repository.words.onEach { list ->
                if (list.isNotEmpty()) {
                    currentIndex = 0
                    currentWord.value = list[0]
                    progress.floatValue = 0f
                    answer = ""
                }

            }.launchIn(viewModelScope)
        }
    }

    fun onNextClick() {

        if (wordsList.isEmpty()) return
        if (isQuizFinished) {
            isQuizFinished=false
            points = 0
        currentIndex = -1
        wrongAnswers.clear()
       } else {
            if (currentIndex>= 0){
                if (isCorrect()) {
                    points++
                } else wrongAnswers.add(
                    quizWrongAnswers (word = wordsList[currentIndex].word,
                    pronunciation = wordsList[currentIndex].pronunciation,
                    input = answer)
                )
            }
        }

        if (currentIndex < wordsList.size - 1) {

            currentIndex += 1

            currentWord.value = wordsList[currentIndex]

            progress.floatValue = (currentIndex + 1) / wordsList.size.toFloat()

            answer = ""

        }
        else { isQuizFinished= true

        println("wrong answers ${wrongAnswers}")}
        println("currentIndex: $currentIndex, Wordlist: ${wordsList[currentIndex]}, ${currentWord.value}")
    }

        fun isCorrect(): Boolean {
            return currentWord.value?.pronunciation == answer
        }



}