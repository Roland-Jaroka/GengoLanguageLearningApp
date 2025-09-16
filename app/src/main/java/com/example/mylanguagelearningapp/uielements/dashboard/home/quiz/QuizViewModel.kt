package com.example.mylanguagelearningapp.uielements.dashboard.home.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.model.QuizManager.quizzes
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.words.ChineseWords

class QuizViewModel: ViewModel() {

    val currentLanguage= UserSettingsRepository.language.value

    val wordsList = if(quizzes.isNotEmpty()) quizzes
    else if (currentLanguage=="jp") JapaneseWords.wordList
    else if (currentLanguage=="cn") ChineseWords.chinseWordsList
    else JapaneseWords.wordList

    var currentIndex by mutableStateOf(0)
        private set

    var currentWord = mutableStateOf<Words?>(wordsList[0])

    var progress = mutableFloatStateOf(0f)

    var answer by mutableStateOf("")
        private set

    var points by mutableStateOf(0)
        private set

    var isQuizFinished by mutableStateOf(false)
        private set

    var wrongAnswers = mutableListOf<Words>()
        private set

    fun onAnswerChange(newvalue: String) {
        answer = newvalue
    }

    fun onNextClick() {

        if (wordsList.isEmpty()) return
        if (isQuizFinished) {
            isQuizFinished=false
            points = 0
        currentIndex = -1
        wrongAnswers.clear()} else {
            if (currentIndex>= 0){
                if (isCorrect()) {
                    points++
                } else wrongAnswers.add(wordsList[currentIndex])
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