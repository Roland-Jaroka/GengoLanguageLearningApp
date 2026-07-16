package com.example.gengolearning.ui.features.dashboard.home.apiwords

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.MyTopAppBar
import com.example.gengolearning.ui.components.WordCard
import com.example.gengolearning.ui.components.global.SearchBar
import com.example.gengolearning.ui.features.dashboard.home.quiz.ErrorModal
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiWordsScreenRoot (viewModel: ApiWordsViewModel = hiltViewModel(),
                    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val tutorial by viewModel.tutorialModal(context).collectAsState(true)

    ApiWordsScreen(
        state = uiState,
        onAction = viewModel::onAction,
        onNavigateBack = {
            navController.popBackStack()
        },
        onNavigateToAddWords = {
            navController.navigate(Route.AddWords(word = it.word,
                pronunciation = it.pronunciation, translation = it.translation))
        },
        tutorial = tutorial,
        context = context
    )
}
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ApiWordsScreen(state: ApiWordsUiState, onAction: (ApiWordsActions)-> Unit = {}, onNavigateBack: () -> Unit = {},
                       onNavigateToAddWords: (Route.AddWords) -> Unit = {},
                       tutorial: Boolean = false,
                       context: Context) {

        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()



        Scaffold(
            topBar = {
                MyTopAppBar(
                    title = stringResource(R.string.dictionary),
                    onBackClick = {
                       onNavigateBack()
                    },
                    modifier = Modifier,
                    actions = {
                        Image(
                            painter = painterResource(R.drawable.infoicon100dp),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 5.dp)
                                .clickable(
                                    onClick = {
                                        onAction(ApiWordsActions.OnSetTutorialWithInfo(true))
                                    }
                                )
                        )
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Column {

                    SearchBar(
                        searchInput = state.searchInput,
                        onValueChange = {

                            onAction(ApiWordsActions.OnSearchInput(it))
                        },
                        onClear = {

                            onAction(ApiWordsActions.OnSearchInput(""))
                        },
                        hasLabel = true
                    )

                    LazyColumn(
                        state = listState
                    ) {
                        if (!state.isLoading) {
                            state.wordList.forEach { word ->
                                item {
                                    WordCard(
                                        word = word.word,
                                        pronunciation = word.pronunciation,
                                        translation = word.translation,
                                        currentLanguage = "jp",
                                        longTap = {
                                            onNavigateToAddWords(
                                                Route.AddWords(
                                                    word.word,
                                                    word.pronunciation,
                                                    word.translation
                                                )
                                            )
                                        },
                                        isLoading = state.isLoading
                                    )
                                }
                            }
                            item { Spacer(modifier = Modifier.height(58.dp)) }
                        } else {
                            repeat(3) {
                                item {
                                    WordCard(
                                        word = "",
                                        pronunciation = "",
                                        translation = "",
                                        currentLanguage = "jp",
                                        isLoading = true
                                    )
                                }
                            }
                        }
                    }


                }
                if (state.wordList.isEmpty() && !state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Image(
                                painterResource(R.drawable.emptybox),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(100.dp)
                                    .padding(bottom = 10.dp)
                            )
                            Text(
                                text = stringResource(R.string.api_words_empty_list),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(start = 5.dp, end = 5.dp),
                                textAlign = TextAlign.Center

                            )
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .imePadding()
                ) {
                    MyAppButton(
                        text = stringResource(R.string.search),
                        onClick = {

                            onAction(ApiWordsActions.OnLoadWords(searchKey = state.searchInput))

                            scope.launch {
                                listState.animateScrollToItem(1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        isLoading = state.isLoading

                    )
                }
            }
        }


        if (tutorial || state.tutorialWithInfo) {
            JishoTutorialModal(
                onClick = {

                    onAction(ApiWordsActions.OnSetTutorialWithInfo(false))
                    onAction(ApiWordsActions.OnSetTutorial(context = context))

                }
            )
        }

        if (state.error == true) {
            ErrorModal(
                onClick = {
                    onAction(ApiWordsActions.OnResetError)
                },
                text = state.modalText?.text ?: R.string.common_error_internet_button
            )
        }
    }

@Preview
@Composable
private fun Preview() {
    ApiWordsScreen(
        state = ApiWordsUiState(),
        context = LocalContext.current
    )
}