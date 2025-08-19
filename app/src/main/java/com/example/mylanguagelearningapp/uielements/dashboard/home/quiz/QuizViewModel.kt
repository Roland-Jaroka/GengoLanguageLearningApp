package com.example.mylanguagelearningapp.uielements.dashboard.home.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.japanesewords.JapaneseWords
import com.example.mylanguagelearningapp.model.Words

class QuizViewModel: ViewModel() {

    val wordsList = JapaneseWords.wordList
    var currentIndex by mutableStateOf(0)
        private set

    var currentWord= mutableStateOf<Words?>(wordsList[0])

    var progress = mutableFloatStateOf(0f)

    var answer by mutableStateOf("")
        private set

    var points by mutableStateOf(0)
        private set

    var isQuizFinished by mutableStateOf(false)

    fun onAnswerChange(newvalue: String){
        answer= newvalue
    }

    fun onNextClick(){

        if (wordsList.isEmpty()) return

        else if (currentIndex < wordsList.size - 1) {

            currentIndex += 1

            currentWord.value = wordsList[currentIndex]

            progress.floatValue = (currentIndex + 1) / wordsList.size.toFloat()

            answer = ""

            isCorrect()
        } else {isQuizFinished= true}

    }

    fun isCorrect(): Boolean{
        return if (currentWord.value?.pronunciation == answer) {
            points++
             true

        } else false


    }


    }