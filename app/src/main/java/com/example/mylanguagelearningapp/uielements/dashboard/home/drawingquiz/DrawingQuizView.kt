package com.example.mylanguagelearningapp.uielements.dashboard.home.drawingquiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White

@Composable
fun DrawingQuizView(viewModel:DrawingCanvasViewModel= viewModel(),
                    navController: NavController) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        println("DrawingQuizView launched: ${viewModel.wordsList}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = if(viewModel.isKanjiReveled) viewModel.currentWord.value!!.word else viewModel.currentWord.value!!.pronunciation,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
        )

        Text(
            text = viewModel.currentWord.value!!.translation,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp, bottom = 1.dp)
        )

       Button(
           modifier = Modifier
               .width(150.dp)
               .align(Alignment.CenterHorizontally),
           colors = ButtonDefaults.buttonColors(
               contentColor = if (viewModel.isKanjiReveled) White else BgBlue,
               containerColor = if (viewModel.isKanjiReveled) BgBlue else White
           ),
           shape = RoundedCornerShape(8.dp),
           border= BorderStroke(2.dp, BgBlue),
           onClick = {
               viewModel.isKanjiReveled= !viewModel.isKanjiReveled
           }
       ) {
           Text(text = "Kanji")
       }



        DrawingCanvas(
            paths = state.paths,
            currentPath = state.currentPath,
            onAction = viewModel::onAction,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Row(modifier = Modifier.fillMaxWidth()) {

            Button(
                modifier = Modifier
                    .padding(12.dp)
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                elevation = ButtonDefaults.buttonElevation(
                    hoveredElevation = 10.dp,
                    pressedElevation = 10.dp,
                    defaultElevation = 5.dp
                ),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    viewModel.onBackClick()
                    viewModel.onAction(DrawingActions.OnClearCanvas)
                },

                ) {

                Image(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 5.dp)
                )

                Text(
                    text = "Back"
                )

            }
            Button(
                modifier = Modifier
                    .padding(12.dp)
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                elevation = ButtonDefaults.buttonElevation(
                    hoveredElevation = 10.dp,
                    pressedElevation = 10.dp,
                    defaultElevation = 5.dp
                ),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    viewModel.onNextClick()
                    viewModel.onAction(DrawingActions.OnClearCanvas)
                },

                ) {

                Text(
                    text = "Next"
                )

                Image(
                    painter = painterResource(R.drawable.foward_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start =  5.dp)
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = White,
                containerColor = Blue
            ),
            elevation = ButtonDefaults.buttonElevation(hoveredElevation = 10.dp, pressedElevation = 10.dp, defaultElevation = 5.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                viewModel.onAction(DrawingActions.OnClearCanvas)
            },

            ) {

            Image(
                painter = painterResource(R.drawable.rubber),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 5.dp)
            )

            Text(
                text = "Clear"
            )
        }

        Text(
            text = "Back to home",
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp)
                .clickable(indication = null,
                    interactionSource = remember{ MutableInteractionSource() }) {

                    navController.navigate("home")
                },
            color = Blue)
    }

}