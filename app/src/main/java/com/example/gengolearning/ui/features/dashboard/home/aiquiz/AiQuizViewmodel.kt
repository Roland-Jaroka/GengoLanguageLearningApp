package com.example.gengolearning.ui.features.dashboard.home.aiquiz


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AiQuizViewmodel @Inject constructor(
    private val repository: LanguageWords,
    userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val currentLanguage = userSettingsRepository.selectedLanguage

    private val _uiState = MutableStateFlow(AiQuizUiState())
    val uiState = combine(_uiState, currentLanguage) { state, language ->
        state.copy(
            currentLanguage = language
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AiQuizUiState()
    )

    private var quizList: List<AiQuiz> = emptyList()

    private var currentIndex = 0


    fun onAction(action: AiQuizActions) {
        when (action) {
            is AiQuizActions.onOptionClick -> {
                if (!_uiState.value.isProcessing && !_uiState.value.isReviewMode) {
                isCorrect(action.option)
                }
            }
            is AiQuizActions.onErrorModalClick -> {
                resetState()
            }
            is AiQuizActions.onLevelClick -> {
                      getAiQuiz(action.language, action.level)
            }
            is AiQuizActions.onReviewMode -> {
                onReviewMode()
            }

            is AiQuizActions.onRestart -> {
                onRestart()
            }
            is AiQuizActions.onNextClick -> {
                    onNextClick()
            }

            is AiQuizActions.onBackQuizClick -> {
                onBackClick()
            }
        }
    }

    private fun isCorrect(answer: String) {
        val currentQuestion = quizList[currentIndex]
        if (answer == currentQuestion.correctAnswer) {
            _uiState.update { it.copy(
                selectedOption = answer,
                isProcessing = true,
                points = it.points + 1
            ) }

            nextQuestion()

        } else {
            _uiState.update { it.copy(
                selectedOption = answer,
                isProcessing = true,
                incorrectAnswers = it.incorrectAnswers + answer
            ) }
            nextQuestion()
        }
    }

    private fun nextQuestion() {


      viewModelScope.launch {
          delay(1000)
          currentIndex++
          if (currentIndex < quizList.size) {
              _uiState.update {
                  it.copy(
                      quiz = quizList[currentIndex],
                      selectedOption = "",
                      isProcessing = false
                  )
              }
          } else {
              _uiState.update {
                  it.copy(
                      quiz = null,
                      selectedOption = "",
                      isProcessing = false,
                      isFinished = true
                  )
              }
          }
      }
    }

    fun onStart() {
        _uiState.update { it.copy(
            showLevelSelectorModal = true
        ) }
    }

    private fun getAiQuiz(language: String, level: String) {
        _uiState.update { it.copy(
            isLoading = true,
            showLevelSelectorModal = false
        )

        }

      viewModelScope.launch {
          try {
             val result = repository.getAiquiz(language, level)
              quizList = result

              println("Result of quiz: ${result.size}")

              _uiState.update { it.copy(
                  quiz = quizList[currentIndex],
                  isLoading = false,
                  totalPoints = result.size
              ) }

          } catch(e: Exception) {
              if (e.message?.contains("you exceeded your current quota") ?: false) {
                  _uiState.update { it.copy(
                      isLoading = false,
                      isError = true,
                      modals = AiQuizModals.LimitError
                  )
                  }
              } else if (e.message?.contains(("Gemini Developer API is overloaded")) ?: false ||
                  e.message?.contains("This model is currently experiencing high demand") ?: false) {

                  _uiState.update { it.copy(
                      isLoading = false,
                      isError = true,
                      modals = AiQuizModals.ServerError
                  )
                  }
              }

              else {

              _uiState.update {
                  it.copy(
                      isLoading = false,
                      isError = true,
                      modals = AiQuizModals.UnknownError(e.message ?: "Unknown error")
                  )
              }

              }
          }
      }
    }

    private fun  resetState() {
        _uiState.update { it.copy(
            isError = false,
            modals = null
        )
        }
    }

    private fun onRestart() {

        currentIndex = 0

        _uiState.update {
            it.copy(
                quiz = quizList[currentIndex],
                selectedOption = "",
                isProcessing = false,
                isFinished = false,
                points = 0,
                incorrectAnswers = emptyList(),
                isReviewMode = false
            )
        }
    }

    private fun onReviewMode() {
        currentIndex = 0
        _uiState.update {
            it.copy(
                quiz = quizList[currentIndex],
                selectedOption = "",
                isProcessing = false,
                isFinished = false,
                isReviewMode = true
            )
        }
    }

    private fun onNextClick() {


        currentIndex++

        if (currentIndex < quizList.size) {
            _uiState.update {
                it.copy(
                    quiz = quizList[currentIndex]
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    quiz = null,
                    isFinished = true
                )
            }
        }
    }

    private fun onBackClick() {
        if (currentIndex > 0) {
            currentIndex--

            _uiState.update {
                it.copy(
                    quiz = quizList[currentIndex]
                )
            }
        }
    }
}