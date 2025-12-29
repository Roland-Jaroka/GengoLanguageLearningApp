package com.example.gengolearning.ui.features.dashboard.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.model.utils.QuizManager.quizzes
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.ButtonCards
import com.example.gengolearning.ui.components.DashboardHeader
import com.example.gengolearning.ui.components.MyListCard
import com.example.gengolearning.ui.components.OnBoardingModal
import com.example.gengolearning.ui.components.WordFilterChips
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeViewModel= hiltViewModel(),
         navController: NavController
) {

    var visible by remember { mutableStateOf(false) }
    var wordsVisibilty by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
     var accumulated by remember { mutableStateOf(0f) }
    val scrollState = rememberScrollState()
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )
    val wordList by viewModel.wordsList.collectAsState()
    val context = LocalContext.current
    val showTutorial by viewModel.showTutorial(context).collectAsState(initial = false)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val userName by viewModel.username.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val isWordVisible = viewModel.isWordVisible
    val isPronunciationVisible = viewModel.isPronunciationVisible
    val isTranslationVisible = viewModel.isTranslationVisible






    LaunchedEffect(Unit) {
        visible = true
        println("Logged in user email ${auth.currentUser?.email}")
        println("current language ${currentLanguage} and $selectedLanguage")

    }


    val currentWord by viewModel.currentWord
    val currentIndex= viewModel.currentIndex

    Scaffold(
        topBar = {
            DashboardHeader(
                titleText = stringResource(R.string.welcome),
                userName = userName,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .verticalScroll(scrollState)
                .padding(paddingValues)

        )
        {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {


                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 1000, delayMillis = 400)
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = "${currentIndex.value + 1}/${wordList.size}",
                            modifier = Modifier
                                .padding(end = 30.dp, top = 12.dp)
                                .align(Alignment.End),
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif
                        )

                        Card(
                            modifier = Modifier
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
                                                wordsVisibilty = !wordsVisibilty
                                            } else {
                                                viewModel.onPreviousClick()
                                                wordsVisibilty = !wordsVisibilty
                                            }
                                            accumulated = 0f
                                        }
                                    })
                                },
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(White)
                        ) {

                            Column(modifier = Modifier.fillMaxSize()) {

                                Row(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .background(BgBlue)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Image(
                                        painter = painterResource(R.drawable.outline_arrow_back),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .padding(start = 10.dp)
                                            .size(25.dp)
                                            .clickable {
                                                viewModel.onPreviousClick()
                                                wordsVisibilty = !wordsVisibilty
                                            }
                                    )
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .weight(4f)
                                    )
                                    {
                                        Image(
                                            painter = painterResource(currentLanguage.flag),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(90.dp)
                                                .padding(end = 5.dp),
                                        )

                                        Text(
                                            text = stringResource(R.string.word_card_todays_word),
                                            color = White,
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )
                                    }

                                    Image(
                                        painter = painterResource(R.drawable.outline_arrow_forward),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 5.dp)
                                            .align(Alignment.CenterVertically)
                                            .size(25.dp)
                                            .weight(0.5f)
                                            .clickable {
                                                viewModel.onNextClick()
                                                wordsVisibilty = !wordsVisibilty
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

                                if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {
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
                                }

                                Text(
                                    text = when {
                                        !viewModel.isTranslationVisible -> ""
                                        currentWord != null -> currentWord!!.translation
                                        else -> ""
                                    },
                                    modifier = Modifier
                                        .padding(top = 10.dp, start = 20.dp),
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.SansSerif
                                )


                            }

                        }
                    } // Card End
                }
                Spacer(modifier = Modifier.height(5.dp))

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {

                    WordFilterChips(
                        selected = !isWordVisible,
                        title = stringResource(R.string.word_button),
                        onClick = {
                            viewModel.onWordClick()
                        }
                    )
                    WordFilterChips(
                        selected = !isPronunciationVisible,
                        title = stringResource(R.string.pronuncitaon_button),
                        onClick = {
                            viewModel.onPronunciationClick()
                        }
                    )
                    WordFilterChips(
                        selected = !isTranslationVisible,
                        title = stringResource(R.string.translation_button),
                        onClick = {
                            viewModel.onTranslationClick()
                        }
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    ButtonCards(
                        modifier = Modifier
                            .padding(end = 10.dp),
                        onClick = {
                            navController.navigate("addwords")

                            AnalyticsHelper.logEvent("addWords_button_dashboard")
                        },
                        title = stringResource(R.string.add_words_title),
                        buttonText = stringResource(R.string.add_words_button),
                        id = R.drawable.writing_icon,
                        buttonId = R.drawable.plus_icon,
                        buttonModifier = Modifier
                            .size(20.dp)
                            .padding(end = 5.dp)
                    )

                    MyListCard(
                        modifier = Modifier,
                        onClick = {
                            navController.navigate("myList")

                            AnalyticsHelper.logEvent("myList_button")
                        },
                        title = stringResource(R.string.my_list_title),
                        buttonText = stringResource(R.string.my_list_button)
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {

                    ButtonCards(
                        title = stringResource(R.string.quizes_button),
                        buttonText = stringResource(R.string.quizes_button),
                        id = R.drawable.quizzes,
                        modifier = Modifier
                            .padding(end = 10.dp),
                        onClick = {
                            quizzes.clear()
                            navController.navigate("quiz")

                            AnalyticsHelper.logEvent("quiz_button_dashboard")

                        },
                        buttonId = R.drawable.quiz_icon
                    )

                    if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {

                        ButtonCards(
                            title = stringResource(R.string.drawing_quiz_button),
                            buttonText = stringResource(R.string.drawing_quiz_button_title),
                            id = R.drawable.caligraphy2,
                            onClick = {
                                quizzes.clear()
                                navController.navigate("drawing")

                                AnalyticsHelper.logEvent("drawing_quiz_button_dashboard")
                            },
                            buttonId = R.drawable.paintingbrush,
                            buttonModifier = Modifier
                                .size(25.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                  if (currentLanguage.code =="jp") {
                      ButtonCards(
                          title = "Search in a Dictionary",
                          buttonText = "Search",
                          id = R.drawable.open_dictionary,
                          buttonId = R.drawable.search,
                          onClick = {
                              navController.navigate("apiWords")
                          }
                      )
                  }
                }

                Spacer(modifier = Modifier.height(100.dp))

            }




        }


        if (showTutorial) {
            OnBoardingModal(
                onClick = {
                    viewModel.setWelcomeTutorial(context)
                },
                sheetState,
            )
        }
    }

}


