package com.example.gengolearning.uielements.dashboard.home.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.QuizManager.quizzes
import com.example.gengolearning.model.Tonemarks.toPinyin
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.model.quizWrongAnswers
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class QuizViewModel @Inject constructor(
    repository: LanguageWords,
    userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val _currentList = MutableStateFlow<List<Words>>(emptyList())
    val currentList = _currentList.asStateFlow()


    val currentLanguage = userSettingsRepository.selectedLanguage.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        userSettingsRepository.languages[0]
    )

    var currentIndex by mutableIntStateOf(0)
        private set

    var currentWord = mutableStateOf<Words?>(null)

    var progress = mutableFloatStateOf(0f)

    var answer by mutableStateOf("")
        private set

    var points by mutableIntStateOf(0)
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
            _currentList.value = quizzes
            currentWord.value = currentList.value[0]
            progress.floatValue = 0f
            answer= ""
        }
        else {
            repository.words.onEach { list ->
                if (list.isNotEmpty()) {
                    currentIndex = 0
                    currentWord.value = list[0]
                    _currentList.value = list
                    progress.floatValue = 0f
                    answer = ""
                }

            }.launchIn(viewModelScope)
        }
    }

    fun onNextClick(currentLanguage: String) {

        if (currentList.value.isEmpty()) return
        if (isQuizFinished) {
            isQuizFinished=false
            points = 0
        currentIndex = -1
        wrongAnswers.clear()
       } else {
            if (currentIndex>= 0){
                if (isCorrect(currentLanguage)) {
                    points++
                } else wrongAnswers.add(
                    quizWrongAnswers (word = currentList.value[currentIndex].word,
                    pronunciation = currentList.value[currentIndex].pronunciation,
                        translation = currentList.value[currentIndex].translation,
                    input = answer)

                )
            }
        }

        if (currentIndex < currentList.value.size - 1) {

            currentIndex += 1

            currentWord.value = currentList.value[currentIndex]

            progress.floatValue = (currentIndex + 1) / currentList.value.size.toFloat()

            answer = ""

        }
        else { isQuizFinished= true

        println("wrong answers $wrongAnswers")}
        println("currentIndex: $currentIndex, Wordlist: ${currentList.value[currentIndex]}, ${currentWord.value}")
    }

        fun isCorrect(currentLanguage: String): Boolean {
            return when (currentLanguage) {
                "jp"-> {
                    currentWord.value?.pronunciation == answer
                }

                "cn" -> {
                    currentWord.value?.pronunciation == answer
                }

                else -> {
                    currentWord.value?.translation.equals(answer, ignoreCase = true)
                }
            }

            }




}