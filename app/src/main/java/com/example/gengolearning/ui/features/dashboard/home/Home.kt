package com.example.gengolearning.ui.features.dashboard.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gengolearning.model.appmodels.DashboardFeatures
import com.example.gengolearning.model.appmodels.DashboardFeaturesList
import com.example.gengolearning.model.appmodels.FeatureType
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.model.appmodels.ProfileImageState
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.ui.components.ButtonCards
import com.example.gengolearning.ui.components.DashboardHeader
import com.example.gengolearning.ui.components.NewsBottomSheetModal
import com.example.gengolearning.ui.components.NewsCard
import com.example.gengolearning.ui.components.WordFilterChips
import com.example.gengolearning.ui.features.dashboard.home.mylist.QuizIsEmptyModal
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.AppColorTheme
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import com.gengolearning.app.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoot(viewModel: HomeViewModel= hiltViewModel(),
         navController: NavController
) {

    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )

    //filter the features based on the selected language so only the right ones are visible for the right language
    val visibleFeatures = remember(currentLanguage) {
        DashboardFeaturesList.list.filter {
            it.supportedLanguages?.contains(currentLanguage) ?: true
        }
    }

    val image by viewModel.image.collectAsState()




    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is HomeUiEvents.NavigateToQuiz -> {
                    navController.navigate(Route.Quiz)
                }

                is HomeUiEvents.UnableToSync -> {
                    snackbarHostState.showSnackbar(
                        message = "Unable to sync the data",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Home(
        state = state,
        onAction = viewModel::HomeActions,
        image = image,
        onNavigate = {navController.navigate(it)},
        visible = true,
        currentLanguage = currentLanguage,
        visibleFeatures = visibleFeatures,
        snackbarHostState = snackbarHostState
    )
}
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Home(state: HomeUiState, onAction: (HomeActions)-> Unit = {},
             image: ProfileImageState,
             onNavigate: (Route) -> Unit = {},
             visible: Boolean,
             currentLanguage: Language,
             visibleFeatures: List<DashboardFeatures>,
             snackbarHostState: SnackbarHostState
    ) {
        val sheetState = rememberModalBottomSheetState()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val scrollState = rememberScrollState()
        val currentAppLanguage = AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() ?: "en"
        val pageState = rememberPagerState(pageCount = { state.news.size })



        Scaffold(
            topBar = {
                DashboardHeader(
                    titleText = stringResource(R.string.welcome),
                    userName = state.userName,
                    scrollBehavior = scrollBehavior,
                    image = when (val image = image) {
                        is ProfileImageState.LoadedImage -> rememberAsyncImagePainter(image.image)
                        else -> painterResource(R.drawable.profile)
                    },
                    onClick = {
                        onNavigate(Route.Profile)
                    },
                    isLoading = image is ProfileImageState.Loading
                )
            },
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 100.dp)
                )
            },
            containerColor = MaterialTheme.colorScheme.background

        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
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

                       HomeCard(
                           currentIndex = state.currentIndex,
                           wordListSize = state.wordList.size,
                           onNextClick = {
                               onAction(HomeActions.OnNextClick)
                           },
                           onPreviousClick = {
                               onAction(HomeActions.OnPreviousClick)
                           },
                           currentLanguage = currentLanguage,
                           isWordVisible = state.isWordVisible,
                           isTranslationVisible = state.isTranslationVisible,
                           isPronunciationVisible = state.isPronunciationVisible,
                           currentWord = state.currentWord
                       )
                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        WordFilterChips(
                            selected = !state.isWordVisible,
                            title = stringResource(R.string.word_button),
                            onClick = {
                                onAction(HomeActions.OnWordClick)
                            },
                        )
                        if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {
                            WordFilterChips(
                                selected = !state.isPronunciationVisible,
                                title = stringResource(R.string.pronuncitaon_button),
                                onClick = {
                                    onAction(HomeActions.OnPronounciationClick)
                                },
                            )
                        }
                        WordFilterChips(
                            selected = !state.isTranslationVisible,
                            title = stringResource(R.string.translation_button),
                            onClick = {
                                onAction(HomeActions.OnTranslationClick)
                            },
                        )
                    }

                    SyncCard(
                        isSynced = state.synchronized,
                        onNotSyncedInfo = {
                            onAction(HomeActions.OnGetData)
                        },
                        onSyncedInfo = {
                            onAction(HomeActions.OnShowSyncedModal)
                        },
                        isLoading = state.isSyncing
                    )

                    FlowRow(
                        maxItemsInEachRow = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 10.dp, start = 5.dp)
                    ) {
                        visibleFeatures.forEach { feature ->
                            ButtonCards(
                                features = feature,
                                flag = currentLanguage.flag,
                                language = stringResource(currentLanguage.name),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp),
                                onClick = {
                                    when (feature.type) {
                                        FeatureType.NewWords -> onNavigate(feature.route)
                                        FeatureType.MyList -> onNavigate(feature.route)
                                        FeatureType.Quizzes -> onAction(HomeActions.OnQuizClick)
                                        FeatureType.DrawingQuiz -> onNavigate(feature.route)
                                        FeatureType.Dictionary -> onNavigate(feature.route)
                                        FeatureType.LanguageChange -> onNavigate(feature.route)
                                        FeatureType.AiQuiz -> onNavigate(feature.route)
                                    }
                                },
                                onNavigate = {onNavigate(Route.LearningLanguage)}
                            )
                        }

                    }

                    HorizontalPager(
                        state = pageState,
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) { page ->

                        val item = state.news[page]

                        NewsCard(
                            imageUrl = item.imageUrl,
                            title = when (currentAppLanguage) {
                                "en" -> item.newsEn
                                "ja" -> item.newsJp
                                "hu" -> item.newsHu
                                else -> item.newsEn
                            },
                            message = when (currentAppLanguage) {
                                "en" -> item.messageEn
                                "ja" -> item.messageJp
                                "hu" -> item.messageHu
                                else -> item.messageEn
                            },
                            clickable = item.clickable,
                            onClick = {
                                onAction(HomeActions.OnOpenNewsModal)
                            }
                        )

                    }

                    Spacer(modifier = Modifier.height(110.dp))


                }


            }
        }

        if (state.newsModal) {
            NewsBottomSheetModal(
                onDismiss = {
                    onAction(HomeActions.OnCloseNewsModal)
                },
                onClick = {
                    onAction(HomeActions.OnSyncWithCloud)
                }
            )
        }

        if (state.quizIsEmptyModal) {
            QuizIsEmptyModal(
                sheetState = sheetState,
                onDismiss = {
                   onAction(HomeActions.OnDismissQuizEmptyModal)
                }
            )
        }

        if (state.syncedInfoModal) {
            SyncedInfoModal(
                onDismiss = {
                    onAction(HomeActions.OnShowSyncedModal)
                }
            )
        }

    }

@Preview
@Composable
private fun Preview() {
    MyLanguageLearningAppTheme(appColorTheme = AppColorTheme.SUNSET) {
        Home(
            state = HomeUiState(
                currentWord = Words(
                    "Test",
                    "Test",
                    "Test"
                ),
                synchronized = true
            ),
            image = ProfileImageState.Empty,
            visible = true,
            currentLanguage = Languages.languagesList[0],
            visibleFeatures = DashboardFeaturesList.list,
            snackbarHostState = SnackbarHostState()
        )
    }

}






