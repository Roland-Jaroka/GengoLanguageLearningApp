package com.example.mylanguagelearningapp.uielements.dashboard.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.CountryFlags
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.model.QuizManager.quizzes
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.abs
import androidx.compose.foundation.layout.*


@Composable
fun Home(viewModel: HomeViewModel= viewModel(),
         navController: NavController
) {

    var visible by remember { mutableStateOf(false) }
    var wordsVisibilty by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
     var accumulated by remember { mutableStateOf(0f) }
    val scrollState = rememberScrollState()
    val currentLanguage = UserSettingsRepository.language.value



    LaunchedEffect(Unit) {
        viewModel.loadData()
        visible = true
        println("Logged in user email ${auth.currentUser?.email}")
        println("current language ${currentLanguage}")

    }

    val currentWord by viewModel.currentWord
    val currentIndex= viewModel.currentIndex


    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)
        .verticalScroll(scrollState)
        .padding(bottom = 120.dp))
    {
        Column(modifier = Modifier
            .fillMaxSize()) {

            Text(
                text = "Home",
                fontSize = 50.sp,
                modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp)
                    .align(Alignment.CenterHorizontally),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = BgBlue
            )


            AnimatedVisibility(
                visible = visible,
                enter= slideInVertically( initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween (durationMillis = 1000, delayMillis = 400))
            ) {
            Column(modifier = Modifier.fillMaxWidth()){

            Text(
                text = "${currentIndex.value + 1}/${viewModel.wordsList.size}",
                modifier = Modifier
                    .padding(end = 30.dp, top = 12.dp)
                    .align(Alignment.End),
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            )

            Card(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(start = 12.dp, end = 12.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->

                        accumulated += dragAmount
                        change.consume()
                        val treshold = 200f
                        if (abs(accumulated) > treshold) {
                        if (accumulated > 0) {
                            viewModel.onNextClick()
                            wordsVisibilty= !wordsVisibilty
                        } else {
                            viewModel.onPreviousClick()
                            wordsVisibilty= !wordsVisibilty
                        }
                            accumulated = 0f }
                        })
                },
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(White),
                border = BorderStroke(2.dp, Blue)) {

                Column(modifier = Modifier.fillMaxSize()) {

                    Row(modifier = Modifier
                        .height(100.dp)
                        .background(BgBlue)
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            painter = painterResource(R.drawable.outline_arrow_back),
                            contentDescription = null,
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(start = 10.dp)
                                .size(25.dp)
                                .clickable{
                                    viewModel.onPreviousClick()
                                    wordsVisibilty= !wordsVisibilty
                                }
                        )
                        Row(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(4f))
                        {
                            Image(
                                painter = painterResource(
                                    if (currentLanguage== "jp") R.drawable.japanese else CountryFlags.CHINESE.resID
                                ),
                                contentDescription = null
                            )

                            Text(
                                text = "Today's words to learn",
                                color = White,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }

                        Image(painter = painterResource(R.drawable.outline_arrow_forward),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 5.dp)
                                .align(Alignment.CenterVertically)
                                .size(25.dp)
                                .weight(0.5f)
                                .clickable{
                                   viewModel.onNextClick()
                                    wordsVisibilty= !wordsVisibilty
                                })
                    }

                      Text(
                          text = when {
                              !viewModel.isWordVisible -> ""
                              currentWord != null -> currentWord!!.word
                              else -> ""
                          },
                          modifier = Modifier
                              .padding(top = 20.dp, start = 20.dp),
                          fontSize = 30.sp,
                          fontFamily = FontFamily.SansSerif,
                          fontWeight = FontWeight.Bold,
                          color = BgBlue
                      )

                    Text(
                        text = when {
                            !viewModel.isPronunciationVisible -> ""
                            currentWord != null -> currentWord!!.pronunciation
                            else -> ""
                        },
                        modifier = Modifier
                            .padding(top = 5.dp, start = 20.dp),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = BgBlue
                    )

                    Text(
                        text = when{
                            !viewModel.isTranslationVisible -> ""
                            currentWord!= null -> currentWord!!.translation
                            else -> ""
                        },
                        modifier = Modifier
                            .padding(top = 10.dp, start = 20.dp),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )


                }

            } } // Card End
                }
            Spacer(modifier = Modifier.height(5.dp))

            Row(modifier= Modifier.fillMaxWidth()){

                Button(
                    onClick = {viewModel.onWordClick()},
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f),
                    colors= ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.isWordVisible) White else BgBlue,
                        contentColor = if (viewModel.isWordVisible) BgBlue else White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, BgBlue)
                ){
                    Text(text = "Word")
                }

                Button(
                    onClick = {viewModel.onPronunciationClick()},
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f),
                    colors= ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.isPronunciationVisible)White else BgBlue,
                        contentColor = if (viewModel.isPronunciationVisible) BgBlue else White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, BgBlue)
                ){

                    Text(text = "Pronunciation",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }

                Button(
                    onClick = {viewModel.onTranslationClick()},
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f),
                    colors= ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.isTranslationVisible) White else BgBlue,
                        contentColor = if (viewModel.isTranslationVisible) BgBlue else White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, BgBlue)
                ){
                    Text(text = "Translation",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }

            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                        .height(50.dp)
                        ,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        containerColor = BgBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(hoveredElevation = 10.dp, pressedElevation = 10.dp, defaultElevation = 5.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        navController.navigate("addwords")
                    },

                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                        )

                    Text(
                        text = "New Word"
                    )
                }

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        containerColor = BgBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(hoveredElevation = 10.dp, pressedElevation = 10.dp, defaultElevation = 5.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        navController.navigate("myList")
                    },

                    ) {

                    Image(
                        painter = painterResource(R.drawable.list_icon),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    Text(
                        text = "My List"
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
                    containerColor = BgBlue
                ),
                elevation = ButtonDefaults.buttonElevation(hoveredElevation = 10.dp, pressedElevation = 10.dp, defaultElevation = 5.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    quizzes.clear()
                    navController.navigate("quiz")

                },

                ) {

                Image(
                    painter = painterResource(R.drawable.quiz_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 5.dp)
                )

                Text(
                    text = "Quizzes"
                )
            }

            //Drawing Function

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = BgBlue
                ),
                elevation = ButtonDefaults.buttonElevation(hoveredElevation = 10.dp, pressedElevation = 10.dp, defaultElevation = 5.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    quizzes.clear()
                    navController.navigate("drawing")

                },

                ) {

                Image(
                    painter = painterResource(R.drawable.paintingbrush),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 5.dp)
                )

                Text(
                    text = "Drawing"
                )
            }


        }

    }

}


