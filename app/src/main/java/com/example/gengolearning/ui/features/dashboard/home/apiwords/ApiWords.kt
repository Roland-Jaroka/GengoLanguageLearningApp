package com.example.gengolearning.ui.features.dashboard.home.apiwords

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
fun ApiWordsScreen (viewModel: ApiWordsViewModel = hiltViewModel(),
                    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val searchInput = viewModel.searchInput
    val tutorial by viewModel.tutorialModal(context).collectAsState(true)
    var tutorialWithInfo by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(R.string.dictionary),
                onBackClick = {
                    navController.popBackStack()
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
                                    tutorialWithInfo = true
                                }
                            )
                    )
                }
            ) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column {

                SearchBar(
                    searchInput = searchInput,
                    onValueChange = {
                        viewModel.onSearchInput(it)
                    },
                    onClear = {
                        viewModel.onSearchInput("")
                    },
                    hasLabel = true
                )

                LazyColumn(
                    state = listState
                ) {
                    if (!uiState.isLoading) {
                        uiState.wordList.forEach { word ->
                            item {
                                WordCard(
                                    word = word.word,
                                    pronunciation = word.pronunciation,
                                    translation = word.translation,
                                    currentLanguage = "jp",
                                    longTap = {
                                        navController.navigate(Route.AddWords(word.word,
                                            word.pronunciation,
                                            word.translation))
                                    },
                                    isLoading = uiState.isLoading
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
            if (uiState.wordList.isEmpty() && !uiState.isLoading) {
                Box(modifier = Modifier
                    .fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Image(
                            painterResource(R.drawable.emptybox),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
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
                        viewModel.loadWordsFromApi(
                            searchKey = searchInput
                        )
                        scope.launch {
                            listState.animateScrollToItem(1)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BgBlue,
                        contentColor = White
                    ),
                    isLoading = uiState.isLoading

                )
            }
        }
    }


    if (tutorial || tutorialWithInfo) {
        JishoTutorialModal(
            onClick = {
                tutorialWithInfo = false
                viewModel.setTutorial(context)

            }
        )
    }

    if (uiState.error == true) {
        ErrorModal(
            onClick = {
                viewModel.resetError()
            },
            text = uiState.modalText!!.text
        )
    }

}
