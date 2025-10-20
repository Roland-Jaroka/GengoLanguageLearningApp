package com.example.mylanguagelearningapp.uielements.dashboard.home.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.Red
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton

@Composable
fun QuizUi(viewModel: QuizViewModel = viewModel(),
           navController: NavController
) {


    val wrongAnswers = viewModel.wrongAnswers
    val wordList = viewModel.wordsList
    var currentWord by viewModel.currentWord
    val progress by viewModel.progress
    val isQuizFinished = viewModel.isQuizFinished



    Box(modifier= Modifier.fillMaxSize()){

        Column(
            modifier= Modifier.fillMaxSize()) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
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
                    text = currentWord?.word?:"",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                Text(text = currentWord?.translation?:"",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                maxLines = 1
                            )

                            MyAppButton(
                                onClick = {
                                    viewModel.onNextClick()
                                },
                                text = "Next",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(Blue)
                            )

                            Text(
                                text = "Back to home",
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        navController.navigate("home") {
                                            popUpTo("home") {
                                                inclusive = true
                                            }
                                        }

                                    },
                                color = Blue
                            )
                        }

                    }
                } else{
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        ) {
                            items(wrongAnswers) {
                                Row(modifier = Modifier.align(Alignment.Center))
                                {
                                    Text(
                                        text = "${it.word} - ${it.pronunciation} - ",
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

            if (isQuizFinished){
                MyAppButton(
                    onClick = {
                        viewModel.onNextClick()
                    },
                    text = "Restart",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(Blue)
                )

                Text(
                    text = "Back to home",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navController.navigate("home") {
                                popUpTo("home") {
                                    inclusive = true
                                }
                            }

                        },
                    color = Blue
                )
            }



        }

    }





}

