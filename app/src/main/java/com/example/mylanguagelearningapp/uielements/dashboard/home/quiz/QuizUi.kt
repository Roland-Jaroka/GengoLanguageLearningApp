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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@Composable
fun QuizUi(viewModel: QuizViewModel = viewModel(),
           navController: NavController
) {


    val wrongAnswers = viewModel.wrongAnswers


    Box(modifier = Modifier
        .fillMaxSize()) {
        Column(modifier = Modifier.padding(top = 30.dp)) {

            Text(
                text = "Quiz",
                fontSize = 50.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 30.dp),
            )

            LinearProgressIndicator(
                progress = {viewModel.progress.value},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                color = Blue
            )

            Text(
                text = "${viewModel.currentIndex + 1}/${viewModel.wordsList.size}",
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )

            Text(
                text = if (viewModel.isQuizFinished) {"${viewModel.points}/${viewModel.wordsList.size} "}
                else {viewModel.currentWord.value!!.word},
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 30.dp, start = 10.dp),
                lineHeight = 55.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (viewModel.isQuizFinished) {"Points"}
                   else {viewModel.currentWord.value!!.translation},
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )
            if (!viewModel.isQuizFinished) {

                OutlinedTextField(
                    value = viewModel.answer,
                    onValueChange = { viewModel.onAnswerChange(it) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp, bottom = 30.dp),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Answer") }
                )
            }

            if (viewModel.isQuizFinished) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
                    .heightIn(min= 0.dp, max= 300.dp)
                    .border(BorderStroke(2.dp, Blue))
                    )
                {
                    LazyColumn {
                        items(wrongAnswers){
                           Row(modifier = Modifier.align(Alignment.Center))
                           { Text(text = "${it.word} - ${it.pronunciation} - ",
                                fontSize = 30.sp,
                                modifier = Modifier
                                    .padding(start = 5.dp),
                                lineHeight = 40.sp)
                               Text(text = it.input,
                                   fontSize = 30.sp,
                                   modifier = Modifier
                                       .padding(start = 5.dp),
                                   lineHeight = 40.sp,
                                   color= Red
                               )}
                        }
                    }

                }

            }


            Button(
                onClick = { viewModel.onNextClick() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (viewModel.isQuizFinished) "Restart" else "Next"
                )
            }

            Text(
                text = "Back to home",
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {

                        navController.navigate("home")
                    },
                color = Blue)



        }
    }
}

