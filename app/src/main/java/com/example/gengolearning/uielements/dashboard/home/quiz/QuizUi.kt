@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gengolearning.uielements.dashboard.home.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.example.gengolearning.uielements.uimodels.MyTopAppBar
import com.gengolearning.app.R

@Composable
fun QuizUi(viewModel: QuizViewModel = hiltViewModel(),
           navController: NavController
) {


    val wrongAnswers = viewModel.wrongAnswers
    val wordList = viewModel.wordsList
    var currentWord by viewModel.currentWord
    val progress by viewModel.progress
    val isQuizFinished = viewModel.isQuizFinished
    val selectedLanguage by viewModel.currentLanguage.collectAsState()
    val currentLanguage = selectedLanguage.code
    val points = viewModel.points




    Scaffold(
        topBar = {
            MyTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.quizes_button),
                route = "home",
                navController = navController
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Blue
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(200.dp),
                    colors = CardDefaults.cardColors(White),
                    elevation = CardDefaults.cardElevation(20.dp)
                ) {
                    Text(
                        text = if (isQuizFinished) "$points/${wordList.size}" else currentWord?.word
                            ?: "",
                        fontSize = 50.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = if (currentLanguage == "jp" || currentLanguage == "cn") 20.dp else 50.dp)
                    )
                    if (currentLanguage == "jp" || currentLanguage == "cn") {
                        Text(
                            text = if (isQuizFinished) "" else currentWord?.translation ?: "",
                            fontSize = 40.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    if (isQuizFinished){
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 10.dp)
                                .clickable{
                                    viewModel.onNextClick(currentLanguage)
                                }
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
                    }

                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 5.dp)
                        .height(400.dp),
                    colors = CardDefaults.cardColors(White),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    if (!isQuizFinished) {

                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "${viewModel.currentIndex + 1}/${wordList.size}",
                                fontSize = 30.sp,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 40.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {


                                OutlinedTextField(
                                    value = viewModel.answer,
                                    onValueChange = {
                                        viewModel.onAnswerChange(it)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .fillMaxWidth()
                                        .padding(bottom = 30.dp, start = 10.dp, end = 10.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    label = { Text(text = "Answer") },
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            viewModel.onNextClick(currentLanguage)
                                        })
                                )

                                MyAppButton(
                                    onClick = {
                                        viewModel.onNextClick(currentLanguage)
                                    },
                                    text = stringResource(R.string.next_Button),
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(Blue)
                                )
                            }

                        }
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
                                                    "${it.word} - ${it.pronunciation} - "
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


            }

        }
    }




}

