package com.example.gengolearning.ui.features.dashboard.home.aiquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.ui.components.ErrorModal
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.MyTopAppBar
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R


@Composable
fun AiQuizUiRoot(
    viewmodel: AiQuizViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewmodel.onStart()


    }

    AiQuizUI(
        state = state,
        onAction = viewmodel::onAction,
        onBackClick = {
            navController.popBackStack()
        },
        currentLanguage = state.currentLanguage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiQuizUI(
    state: AiQuizUiState,
    onAction: (AiQuizActions) -> Unit,
    onBackClick: () -> Unit = {},
    currentLanguage: Language
) {
    val sheetState = rememberModalBottomSheetState()
    rememberCoroutineScope()

    Scaffold(
        topBar = {
            MyTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.ai_quiz_title),
                onBackClick = {
                    onBackClick()
                }
            )
        }
    ) { paddingValues ->
        Box {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .navigationBarsPadding()
                .fillMaxSize()
                .background(White)
        ) {

            QuestionCard(
                question =
                    if (!state.isFinished) state.quiz?.question ?: ""
                    else "${state.points}/${state.totalPoints}",
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            state.quiz?.options?.forEach { option ->
                AnswerCard(
                    answer = option,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .weight(1f),
                    onClick = {
                        onAction(AiQuizActions.onOptionClick(option))
                    },
                    isCorrect = option == state.selectedOption && option == state.quiz.correctAnswer ||
                            option == state.quiz.correctAnswer && state.selectedOption != "" && state.selectedOption != option ||
                            option == state.quiz.correctAnswer && state.isReviewMode,
                    isFalse = state.selectedOption == option && option != state.quiz.correctAnswer ||
                            state.quiz.correctAnswer != option && state.incorrectAnswers.contains(
                        option
                    )
                )
            }

            if (state.isFinished) {
                MyAppButton(
                    onClick = {
                        onAction(AiQuizActions.onReviewMode)
                    },
                    text = stringResource(R.string.review_button),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                )

                MyAppButton(
                    onClick = {
                        onAction(AiQuizActions.onRestart)
                    },
                    text = stringResource(R.string.ai_quiz_restart_button),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red
                    )
                )
            }

        }

       if (state.isReviewMode && !state.isFinished) {
        ArrowButton(left = true,
            modifier = Modifier
                .align(Alignment.CenterStart),
            onClick = {
                onAction(AiQuizActions.onBackQuizClick)
            })
        ArrowButton(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            onClick = {
                onAction(AiQuizActions.onNextClick)
            }
        )
        }
    }


    }

    if (state.isLoading) {
        AiQuizLoadingScreen()
    }

    if (state.modals is AiQuizModals.UnknownError) {
        ErrorModal(
            sheetState = sheetState,
            onClick = {
                        onAction(AiQuizActions.onErrorModalClick)
                        onBackClick()
            },
            text = state.modals.error
        )
    }

    if (state.modals is AiQuizModals.LimitError) {
        ErrorModal(
            sheetState = sheetState,
            onClick = {
                        onAction(AiQuizActions.onErrorModalClick)
                        onBackClick()
            },
            text = stringResource(R.string.ai_quiz_limit_error)
        )
    }

    if (state.modals is AiQuizModals.ServerError) {
        ErrorModal(
            sheetState = sheetState,
            onClick = {
                onAction(AiQuizActions.onErrorModalClick)
                onBackClick()
            },
            text = stringResource(R.string.ai_quiz_server_error)
        )
    }



    if (state.showLevelSelectorModal) {

        LevelSelectorModal(
            currentLanguage = currentLanguage,
            onClick = {
                onAction(AiQuizActions.onLevelClick(language = currentLanguage.englishName , level = it))
            },
            onBack = {
                onBackClick()
            }
        )
    }

}

@Preview
@Composable
private fun Preview() {
    AiQuizUI(
        state = AiQuizUiState(
            quiz = AiQuiz(
                question = "明日は土曜日です。昨日は木曜日でしたね。今日は何のひ",
                options = listOf(
                    "これはとても長い文章になっておりますので、たぶんこれで十分だと思っております。", "これはとても長い文章になっておりますので、たぶんこれで十分だと思っております",
                    "これはとても長い文章になっておりますので、たぶんこれで十分だと思っております, これはとても長い文章になっておりますので、たぶんこれで十分だと思っております",
                    "これはとても長い文章になっておりますので、たぶんこれで十分だと思っております",
                    "これはとても長い文章になっておりますので、たぶんこれで十分だと思っております"
                ),
                correctAnswer = "Saturday"
            ),
            isFinished = false,
            isReviewMode = true
        ),
        onAction =  {},
        currentLanguage = Languages.languagesList[0],
    )
}