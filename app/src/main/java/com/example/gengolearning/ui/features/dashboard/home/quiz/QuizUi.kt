@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gengolearning.ui.features.dashboard.home.quiz

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.QuizModes
import com.example.gengolearning.model.utils.ImeModeAdjustNothing
import com.example.gengolearning.model.utils.QuizManager.quizzes
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.MyTopAppBar
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.LeafGreen
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun QuizUi(viewModel: QuizViewModel = hiltViewModel(),
           navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()
    val wrongAnswers = viewModel.wrongAnswers
    val wordList = uiState.wordList
    val currentWord = if (uiState.wordList.isNotEmpty()) {uiState.wordList[uiState.currentIndex]} else null
    val progress = uiState.progress
    val isQuizFinished = uiState.isQuizFinished
    val selectedLanguage by viewModel.currentLanguage.collectAsState()
    val currentLanguage = selectedLanguage.code
    val points = uiState.points
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val secondInteractionSource = remember { MutableInteractionSource() }
    val secondIsPressed by secondInteractionSource.collectIsPressedAsState()
    val targetScale = if (isPressed) 0.8f else 1f
    val secondTargetScale = if (secondIsPressed) 0.8f else 1f
    var quizModeDialog by rememberSaveable { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current



    //This handles the free space that was
    //there when imepadding was active
    ImeModeAdjustNothing()

    LaunchedEffect(uiState.quizMode) {
        if (uiState.quizMode == QuizModes.CardPlay) {
            viewModel.cardModeList()
        }
    }


    Scaffold(
        topBar = {
            MyTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.quizes_button),
                onBackClick = {
                    navController.popBackStack()
                },
                onBackAction = {
                    quizzes.clear()
                }
            )
        },
        bottomBar = {

            if (!isQuizFinished && uiState.quizMode != QuizModes.CardPlay) {

                    MyAppButton(
                        onClick = {
                            viewModel.onNextClick(currentLanguage, uiState.answer)
                            focusManager.clearFocus()

                        },
                        text = stringResource(R.string.next_Button),
                        colors = ButtonDefaults.buttonColors(Blue),
                        modifier = Modifier
                            .navigationBarsPadding()
                            .imePadding()
                    )
                }
        },
    ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState)

            ) {

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        color = Blue
                    )



                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(230.dp),
                        colors = CardDefaults.cardColors(White),
                        elevation = CardDefaults.cardElevation(20.dp),
                        border = if (uiState.isError) {
                            BorderStroke(2.dp, Red)
                        } else if (uiState.isCorrect) {
                            BorderStroke(2.dp, LeafGreen)
                        } else null

                    ) {
                        Text(
                            text = if (isQuizFinished) "$points/${wordList.size}" else when (uiState.quizMode) {
                                QuizModes.TranslationQuiz -> currentWord?.word ?: ""
                                QuizModes.WordQuiz -> currentWord?.translation ?: ""
                                QuizModes.PronounciationQuiz -> currentWord?.word ?: ""
                                QuizModes.CardPlay -> currentWord?.translation ?: ""
                            },
                            fontSize = 50.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 60.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    top = if (currentLanguage == "jp" || currentLanguage == "cn" || wrongAnswers.isNotEmpty()) 20.dp else 50.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                )
                        )
                        if (!isQuizFinished) {

                            Text(
                                text = when (uiState.quizMode) {
                                    QuizModes.TranslationQuiz -> currentWord?.pronunciation ?: ""
                                    QuizModes.WordQuiz -> ""
                                    QuizModes.PronounciationQuiz -> currentWord?.translation ?: ""
                                    QuizModes.CardPlay -> ""
                                },
                                fontSize = 40.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )

                            Text(
                                text = if (uiState.isError) {
                                    when (uiState.quizMode) {
                                        QuizModes.PronounciationQuiz -> currentWord?.pronunciation
                                            ?: ""

                                        QuizModes.TranslationQuiz -> currentWord?.translation ?: ""
                                        QuizModes.WordQuiz -> currentWord?.word ?: ""
                                        QuizModes.CardPlay -> ""
                                    }
                                } else "",

                                fontSize = 40.sp,
                                color = Red,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(start = 20.dp, end = 20.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                        }

                        if (isQuizFinished) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {


                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 10.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            enabled = true,
                                            onClickLabel = null,
                                            role = null
                                        ) {
                                            viewModel.onRestart()
                                        }
                                        .scale(targetScale)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.restarticon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                            .size(45.dp)

                                    )

                                    Text(
                                        text = stringResource(R.string.restart_button),
                                        fontSize = 30.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    )
                                }

                                if (wrongAnswers.isNotEmpty() && uiState.quizMode != QuizModes.CardPlay) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(bottom = 10.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = secondInteractionSource
                                            ) {
                                                viewModel.onOnlyWrongAnswersRestart()
                                            }
                                            .scale(secondTargetScale)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.restartpartiallyicon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 5.dp)
                                                .size(45.dp)

                                        )

                                        Text(
                                            text = stringResource(R.string.restart_partially_button),
                                            fontSize = 30.sp,
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                        )
                                    }
                                }
                            }

                        }

                    }


                   if (uiState.quizMode != QuizModes.CardPlay) {

                       Surface(
                           modifier = if (!isQuizFinished) Modifier
                               .fillMaxWidth()
                               .height(200.dp)
                               .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 5.dp)
                               .animateContentSize()
                           else Modifier
                               .fillMaxWidth()
                               .weight(1.5f)
                               .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 5.dp)
                               .animateContentSize(),
                           shadowElevation = 10.dp,
                           shape = RoundedCornerShape(10.dp),
                           color = White
                       ) {
                           if (!isQuizFinished) {

                               OutlinedTextField(
                                   value = uiState.answer,
                                   onValueChange = {
                                       viewModel.onAnswerChange(it)
                                   },
                                   keyboardOptions = KeyboardOptions(
                                       imeAction = ImeAction.Done
                                   ),
                                   keyboardActions = KeyboardActions(
                                       onDone = {
                                           viewModel.onNextClick(currentLanguage, uiState.answer)
                                       }),
                                   shape = RoundedCornerShape(10.dp),
                                   placeholder = {
                                       Text(
                                           text = "Type your answer here"
                                       )
                                   },
                                   isError = uiState.isError,
                                   colors = TextFieldDefaults.colors(
                                       focusedIndicatorColor = Blue,
                                       focusedContainerColor = White,
                                       unfocusedContainerColor = White,
                                       cursorColor = Blue,
                                       errorContainerColor = White,
                                   ),
                               )


                           } else {
                               Box(
                                   modifier = Modifier
                                       .fillMaxSize()
                               ) {
                                   LazyColumn(
                                       modifier = Modifier
                                           .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                                   ) {
                                       items(wrongAnswers) {
                                           LazyRow(
                                               modifier = Modifier
                                                   .align(Alignment.Center)
                                           )
                                           {

                                               item {
                                                   Text(
                                                       text = if (currentLanguage == "jp" || currentLanguage == "cn") {
                                                           "${it.word} - ${it.pronunciation} - ${it.translation} - "
                                                       } else {
                                                           "${it.word} - ${it.translation} - "
                                                       },
                                                       fontSize = 30.sp,
                                                       modifier = Modifier
                                                           .padding(start = 5.dp),
                                                       lineHeight = 40.sp
                                                   )



                                                   Text(
                                                       text = it.input,
                                                       fontSize = 30.sp,
                                                       modifier = Modifier
                                                           .padding(start = 5.dp),
                                                       lineHeight = 40.sp,
                                                       color = Red
                                                   )
                                               }

                                           }
                                       }
                                   }
                               }
                           }
                       }
                   } else {

                       if (!isQuizFinished) {

                           FlowRow(
                               maxItemsInEachRow = 2
                           ) {
                               uiState.cardModeList.forEach { word ->
                                   QuizCards(
                                       word = word,
                                       modifier = Modifier
                                           .weight(1f)
                                           .padding(start = 5.dp, end = 5.dp),
                                       onClick = {
                                           viewModel.onNextClick(currentLanguage, answer = word)
                                       },
                                       isCorrect = uiState.isCorrect && word == currentWord?.word ||
                                               uiState.tappedWord != word && word == currentWord?.word
                                               && uiState.tappedWord != "",

                                       isError = uiState.isError && uiState.tappedWord == word
                                   )
                               }
                           }
                       }

                   }


                }


        if (quizModeDialog) {
            QuizSelectorModal(
                onClick = { mode ->
                    viewModel.onQuizModeChange(mode)
                    quizModeDialog = false
                },
                currentLanguage = currentLanguage,
                onBack = {
                    quizModeDialog = false
                    navController.popBackStack()
                    quizzes.clear()
                },
                onShuffle = {
                    viewModel.shuffleQuizModes()
                    viewModel.setShuffleQuizMode()
                    quizModeDialog = false
                }
            )
        }
    }




}

