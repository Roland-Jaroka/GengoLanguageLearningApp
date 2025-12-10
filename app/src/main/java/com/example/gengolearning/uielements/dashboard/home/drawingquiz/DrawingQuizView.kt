package com.example.gengolearning.uielements.dashboard.home.drawingquiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.MyTopAppBar
import com.gengolearning.app.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingQuizView(viewModel:DrawingCanvasViewModel= hiltViewModel(),
                    navController: NavController) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentWord by viewModel.currentWord

    Scaffold(
        topBar = { MyTopAppBar(
            modifier = Modifier,
            title = stringResource(R.string.drawing_quiz_button),
            route = "Home",
            navController = navController
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (viewModel.isKanjiReveled) currentWord?.word ?: "" else currentWord?.pronunciation ?: "",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )

            Text(
                text = currentWord?.translation ?: "",
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
                border = BorderStroke(2.dp, BgBlue),
                onClick = {
                    viewModel.isKanjiReveled = !viewModel.isKanjiReveled
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

                }
                Button(
                    modifier = Modifier
                        .height(50.dp)
                        .align(Alignment.CenterVertically),
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

                    Image(
                        painter = painterResource(R.drawable.foward_arrow),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )
                }
            }
        }
    }

}