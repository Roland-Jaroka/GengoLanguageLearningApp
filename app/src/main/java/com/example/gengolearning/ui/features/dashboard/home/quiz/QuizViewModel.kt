package com.example.gengolearning.ui.features.dashboard.home.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.utils.QuizManager.quizzes
import com.example.gengolearning.model.utils.Tonemarks.toPinyin
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.appmodels.quizWrongAnswers
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.model.appmodels.QuizModes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val wordList: List<Words> = emptyList(),
    val currentIndex: Int = 0,
    val progress: Float = 0f,
    val answer: String = "",
    val points: Int = 0,
    val isCorrect: Boolean = false,
    val isError: Boolean = false,
    val isQuizFinished: Boolean = false,
    val isProcessing: Boolean = false,
    val quizMode: QuizModes = QuizModes.TranslationQuiz,
    val cardModeList: List<String> = emptyList(),
    val tappedWord: String = "",
    val shuffleMode: Boolean = false
        )
@HiltViewModel
class QuizViewModel @Inject constructor(
    repository: LanguageWords,
    userSettingsRepository: UserSettingsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var initialList = emptyList<Words>()


    val currentLanguage = userSettingsRepository.selectedLanguage.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        userSettingsRepository.languages[0]
    )

    var wrongAnswers = mutableListOf<quizWrongAnswers>()
        private set


    fun onAnswerChange(newvalue: String) {

        if (newvalue.length < 200) {
            _uiState.update {
                it.copy(
                    answer = toPinyin(newvalue)
                )
            }
        }
    }

    init {

        //if there is a custom quiz
        if (quizzes.isNotEmpty()) {

            _uiState.update {
                it.copy(
                    wordList = quizzes.shuffled(),
                    currentIndex = 0,
                    progress = 0f,
                )
            }
            initialList = quizzes
        }
        //if there is no custom quiz
        else {
            repository.words.onEach { list ->
                if (list.isNotEmpty()) {

                    val filteredList = list.filter { it.isOnHomePage == true }
                    //if there are words for homePage
                    if (filteredList.isNotEmpty()) {


                        _uiState.update {
                            it.copy(
                                wordList = filteredList.shuffled(),
                                currentIndex = 0,
                                progress = 0f
                            )
                        }
                        initialList = filteredList
                    }
                    //if there are no words that has isOnHomePage == true
                    //only possible after login if the user has not done filtering yet
                    else {

                        _uiState.update {
                            it.copy(
                                wordList = list.shuffled(),
                                currentIndex = 0,
                                progress = 0f
                            )
                        }
                        initialList = list
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun onNextClick(currentLanguage: String, answer: String) {

         if (_uiState.value.isProcessing) return

        if (_uiState.value.wordList.isEmpty()) return

        if (_uiState.value.quizMode == QuizModes.CardPlay) {
            _uiState.update {
                it.copy(
                    tappedWord = answer
                )
            }
        }
            viewModelScope.launch {

                if (isCorrect(currentLanguage, answer)) {
                    _uiState.update {
                        it.copy(
                            points = it.points + 1,
                            isCorrect = true
                        )
                    }
                    delay(800)
                    nextWord()
                } else {
                    _uiState.update {
                        it.copy(
                            isError = true,
                            isProcessing = true
                        )
                    }
                    wrongAnswers.add(
                        quizWrongAnswers(
                            uiState.value.wordList[uiState.value.currentIndex].word,
                            uiState.value.wordList[uiState.value.currentIndex].pronunciation,
                            uiState.value.wordList[uiState.value.currentIndex].translation,
                            answer
                        )
                    )
                    delay(1000)
                    nextWord()
                }

                _uiState.update {
                    it.copy(isProcessing = false)
                }
            }


    }

    fun isCorrect(currentLanguage: String,
                  answer: String): Boolean {
        val state = uiState.value
        val wordList = state.wordList
        val currentIndex = state.currentIndex

        return when (_uiState.value.quizMode) {

            QuizModes.TranslationQuiz -> {
                wordList[currentIndex].translation
                    .split(",")
                    .map { it.trim() }
                    .any { it.equals(answer.trim(), ignoreCase = true) }
            }

             QuizModes.PronounciationQuiz -> {
                wordList[currentIndex].pronunciation.equals(answer, ignoreCase = true)
            }

            QuizModes.WordQuiz -> {
                wordList[currentIndex].word.equals(answer, ignoreCase = true)
            }

            QuizModes.CardPlay -> {
                wordList[currentIndex].word == answer
            }
        }




    }

    private fun nextWord() {
        val state = uiState.value
        var isQuizFinished = state.isQuizFinished
        var currentIndex = state.currentIndex
        val wordList = state.wordList

        if (isQuizFinished) {
            currentIndex = 0
            isQuizFinished = true
        } else {

            if (currentIndex <= wordList.size -2) {

                currentIndex++
            } else {
                isQuizFinished = true
            }
        }

        _uiState.update {
            it.copy(
                currentIndex = currentIndex,
                isQuizFinished = isQuizFinished,
                isCorrect = false,
                isError = false,
                answer = "",
                tappedWord = "",
                progress = (currentIndex + 1) / wordList.size.toFloat()
            )
        }

        if (state.shuffleMode) {
            shuffleQuizModes()
        }

        if (state.quizMode == QuizModes.CardPlay) {
            cardModeList()
        }



    }

    fun onRestart() {
        _uiState.update {
            it.copy(
                wordList = initialList.shuffled(),
                points = 0,
                currentIndex = 0,
                progress = 0f,
                isQuizFinished = false
            )
        }
        wrongAnswers.clear()
    }

    fun onOnlyWrongAnswersRestart(){
        _uiState.update {
            it.copy(
                wordList = wrongAnswers.map { wrongAnswers ->
                    Words(
                        word = wrongAnswers.word,
                        translation = wrongAnswers.translation,
                        pronunciation = wrongAnswers.pronunciation,
                    )
                }.shuffled(),
                points = 0,
                currentIndex = 0,
                progress = 0f,
                isQuizFinished = false
            )
        }

        wrongAnswers.clear()
    }

    fun onQuizModeChange(quizModes: QuizModes) {
        _uiState.update {
            it.copy(
                quizMode = quizModes

            )
        }
    }

    fun cardModeList() {
        val state = _uiState.value
        val list = state.wordList
        val currentWord = state.wordList[state.currentIndex]
        val currentlyPlayingList = mutableListOf<String>()

        currentlyPlayingList.add(currentWord.word)

        try {

        repeat(3) {currentlyPlayingList.add(
            list.filter { it.word != currentWord.word && it.word !in currentlyPlayingList}.random().word
        )
        }

        } catch (e: Exception) {

        }


        _uiState.update {
            it.copy(
                cardModeList = currentlyPlayingList.shuffled()
            )
        }
    }

    fun shuffleQuizModes(){
        val modes = QuizModes.entries
        val currentLang = currentLanguage.value
        val mode = if (currentLang.code == "jp" || currentLang.code == "cn") {
            modes.filter { it != QuizModes.PronounciationQuiz }
        } else {
            modes
        }
        _uiState.update {
            it.copy(
                quizMode = mode.random()
            )
        }
    }

    fun setShuffleQuizMode(){
        _uiState.update {
            it.copy(
                shuffleMode = true
            )
        }
    }





}