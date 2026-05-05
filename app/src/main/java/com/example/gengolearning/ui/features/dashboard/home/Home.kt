package com.example.gengolearning.ui.features.dashboard.home

import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gengolearning.model.appmodels.DashboardFeaturesList
import com.example.gengolearning.model.appmodels.FeatureType
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.model.appmodels.ProfileImageState
import com.example.gengolearning.ui.components.ButtonCards
import com.example.gengolearning.ui.components.DashboardHeader
import com.example.gengolearning.ui.components.NewsBottomSheetModal
import com.example.gengolearning.ui.components.NewsCard
import com.example.gengolearning.ui.components.WordFilterChips
import com.example.gengolearning.ui.features.dashboard.home.mylist.QuizIsEmptyModal
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.JapaneseFontFamily
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeViewModel= hiltViewModel(),
         navController: NavController
) {
    val sheetState = rememberModalBottomSheetState()
    var visible by remember { mutableStateOf(false) }
    var wordsVisibilty by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
     var accumulated by remember { mutableStateOf(0f) }
    val scrollState = rememberScrollState()
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )
    val wordList by viewModel.wordsList.collectAsState()

    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val userName by viewModel.username.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val isWordVisible = viewModel.isWordVisible
    val isPronunciationVisible = viewModel.isPronunciationVisible
    val isTranslationVisible = viewModel.isTranslationVisible

    val news = viewModel.news

    val newsModal = viewModel.newsModal

    val quizIsEmptyModal = viewModel.quizIsEmptyModal


    val pageState = rememberPagerState(pageCount = {news.size})

    //filter the features based on the selected language so only the right ones are visible for the right language
    val visibleFeatures = remember(currentLanguage) {
        DashboardFeaturesList.list.filter { it.supportedLanguages?.contains(currentLanguage) ?: true }
    }

    val image by viewModel.image.collectAsState()

    val currentAppLanguage =  AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() ?: "en"






    LaunchedEffect(Unit) {
        visible = true
        println("Logged in user email ${auth.currentUser?.email}")
        println("current language ${currentLanguage} and $selectedLanguage")

    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is HomeUiEvents.NavigateToQuiz ->
                    navController.navigate(Route.Quiz)
            }
        }
    }

    val currentWord by viewModel.currentWord
    val currentIndex= viewModel.currentIndex

    Scaffold(
        topBar = {
            DashboardHeader(
                titleText = stringResource(R.string.welcome),
                userName = userName,
                scrollBehavior = scrollBehavior,
                image = when (val image = image) {
                    is ProfileImageState.LoadedImage -> rememberAsyncImagePainter(image.image)
                    else -> painterResource(R.drawable.profile)
                } ,
                onClick = {
                    navController.navigate(Route.Profile)
                },
                isLoading = image is ProfileImageState.Loading
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
                                        modifier = Modifier
                                            .padding(end = 5.dp)
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
                                    fontFamily = JapaneseFontFamily,
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    WordFilterChips(
                        selected = !isWordVisible,
                        title = stringResource(R.string.word_button),
                        onClick = {
                            viewModel.onWordClick()
                        },
                    )
                    if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {
                        WordFilterChips(
                            selected = !isPronunciationVisible,
                            title = stringResource(R.string.pronuncitaon_button),
                            onClick = {
                                viewModel.onPronunciationClick()
                            },
                        )
                    }
                    WordFilterChips(
                        selected = !isTranslationVisible,
                        title = stringResource(R.string.translation_button),
                        onClick = {
                            viewModel.onTranslationClick()
                        },
                    )
                }

                FlowRow(
                    maxItemsInEachRow = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 10.dp, start = 5.dp)
                ) {
                    visibleFeatures.forEach { feature ->
                        ButtonCards(
                            features = feature,
                            navController = navController,
                            flag = currentLanguage.flag,
                            language = stringResource(currentLanguage.name),
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp),
                            onClick = {
                                when (feature.type) {
                                    FeatureType.NewWords -> navController.navigate(feature.route)
                                    FeatureType.MyList -> navController.navigate(feature.route)
                                    FeatureType.Quizzes -> viewModel.onQuizClick()
                                    FeatureType.DrawingQuiz -> navController.navigate(feature.route)
                                    FeatureType.Dictionary -> navController.navigate(feature.route)
                                    FeatureType.LanguageChange -> navController.navigate(feature.route)
                                    FeatureType.AiQuiz -> navController.navigate(feature.route)
                                }
                            }
                        )
                    }

                }


                HorizontalPager(
                    state = pageState,
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) { page->

                    val item = news[page]

                    NewsCard(
                        imageUrl = item?.imageUrl,
                        title = when (currentAppLanguage) {
                            "en" -> item?.newsEn
                            "ja" -> item?.newsJp
                            "hu" -> item?.newsHu
                            else -> item?.newsEn
                        },
                        message = when (currentAppLanguage) {
                            "en" -> item?.messageEn
                            "ja" -> item?.messageJp
                            "hu" -> item?.messageHu
                            else -> item?.messageEn
                        },
                        clickable = item?.clickable ?: false,
                        onClick = {
                            viewModel.openNewsModal()
                        }
                    )

                }

                Spacer(modifier = Modifier.height(110.dp))


            }


        }
    }

    if (newsModal) {
        NewsBottomSheetModal(
            onDismiss = {
                viewModel.closeNewsModal()
            },
            onClick = {
                viewModel.syncroniseWithCloud()
            }
        )
    }

    if (quizIsEmptyModal) {
        QuizIsEmptyModal(
            sheetState = sheetState,
            onDismiss = {
                viewModel.dismissQuizIsEmptyModal()
            }
        )
    }


}






