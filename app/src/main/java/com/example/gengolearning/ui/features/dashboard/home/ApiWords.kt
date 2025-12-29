package com.example.gengolearning.ui.features.dashboard.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.TransParentBackground
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.MyTopAppBar
import com.example.gengolearning.ui.components.WordCard
import com.example.gengolearning.ui.features.dashboard.home.quiz.ErrorModal
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
                title = "Api Words",
                route = "home",
                navController = navController,
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
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = {
                        viewModel.onSearchInput(it)
                    },
                    label ={
                        Text(
                            text = stringResource(R.string.search),
                            color = BgBlue
                        )
                    } ,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_search),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 1,
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchInput.isNotBlank()
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.onSearchInput("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },

                )

                LazyColumn(
                    state = listState
                ) {
                    uiState.wordList.forEach { word ->
                        item {
                            WordCard(
                                word = word.word,
                                pronunciation = word.pronunciation,
                                translation = word.translation,
                                currentLanguage = "jp",
                                longTap = {
                                    navController.navigate("addwords"+
                                    "?word=${word.word}" +
                                    "&pronunciation=${word.pronunciation}" +
                                    "&translation=${word.translation}")
                                }
                            )
                        }
                    }
                    item {  Spacer(modifier = Modifier.height(58.dp)) }
                }

            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .imePadding()
            ) {
                MyAppButton(
                    text = "Search",
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

                )
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TransParentBackground)
                .pointerInput(Unit) {}

        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp),
                color = White
            )
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

    if (uiState.error == "no internet") {
        ErrorModal(
            onClick = {
                viewModel.resetError()
            }
        )
    }

}
