package com.example.gengolearning.ui.features.dashboard.home.aiquiz

import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import kotlinx.serialization.Serializable

data class AiQuizUiState(
    val quiz: AiQuiz? = null,
    val currentLanguage: Language = Languages.languagesList[0],
    val selectedOption: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isFinished: Boolean = false,
    val isProcessing : Boolean = false,
    val showLevelSelectorModal: Boolean = false,
    val points: Int = 0,
    val isReviewMode: Boolean = false,
    val incorrectAnswers: List<String> = emptyList(),
    val modals: AiQuizModals? = null,
    val totalPoints: Int = 0
)

@Serializable
data class AiQuiz(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)


sealed interface AiQuizActions {
    data class onOptionClick(val option: String): AiQuizActions

    data class onLevelClick(val language: String, val level: String): AiQuizActions
    data object onErrorModalClick: AiQuizActions
    data object onReviewMode: AiQuizActions
    data object onRestart: AiQuizActions
    data object onNextClick: AiQuizActions
    data object onBackQuizClick: AiQuizActions

    data object  onRetry: AiQuizActions

}

sealed class AiQuizModals {
    object LimitError: AiQuizModals()
    object ServerError: AiQuizModals()

    data class UnknownError(val error: String): AiQuizModals()

    object NoInternet: AiQuizModals()


}