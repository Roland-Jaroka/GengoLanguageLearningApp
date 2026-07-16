package com.example.gengolearning.ui.features.dashboard.home.addwords

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.theme.AppColorTheme
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import com.gengolearning.app.R

@Composable
fun AddWordsRoot(navController: NavController,
               viewModel: AddWordsViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    val currentLanguage = viewModel.currentLanguage

    val snackbarText = stringResource(R.string.add_words_snackbar)


    val snackbarHostState = remember { SnackbarHostState() }



    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddWordsEvents.showSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = snackbarText,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    AddWords(
        snackbarHostState,
        state = state,
        currentLanguage = currentLanguage,
        onAction = viewModel::onAction,
        onNavigateBack = {navController.popBackStack()})
}
    @Composable
    fun AddWords(snackbarHostState: SnackbarHostState,
                 state: AddWordsUiState,
                 currentLanguage: String,
                 onAction: (AddWordsActions) -> Unit = {},
                 onNavigateBack: () -> Unit = {}) {

        val scrollstate = rememberScrollState()
        val wordInputError = state.wordInputError?.let { id -> stringResource(id) }
        val translationInputError = state.translationInputError?.let { id ->
            stringResource(id)
        }
        val focusRequester = remember { FocusRequester() }


        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState
                )
            },
            containerColor = MaterialTheme.colorScheme.background

        )
        { paddingValues ->


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollstate)
                    .padding(paddingValues)
            ) {

                Column(modifier = Modifier.align(Alignment.Center)) {

                    Text(
                        text = stringResource(R.string.add_words_button),
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = stringResource(R.string.add_words_tolist),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )

                    OutlinedTextField(
                        value = state.word,
                        onValueChange = {
                            onAction(AddWordsActions.OnWordChange(it))
                        },
                        label = { Text(stringResource(R.string.word_button)) },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        isError = wordInputError != null,
                        supportingText = {
                            if (wordInputError != null) {
                                Text(
                                    text = "*$wordInputError",
                                    color = Color.Red
                                )
                            }
                        },
                        shape = RoundedCornerShape(20.dp),

                        )



                    if (currentLanguage == "jp" || currentLanguage == "cn") {
                        OutlinedTextField(
                            value = state.pronunciation,
                            onValueChange = {
                                onAction(AddWordsActions.OnPronunciationChange(it))
                            },
                            label = { Text(stringResource(R.string.pronuncitaon_button)) },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(start = 30.dp, end = 30.dp)
                                .fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                        )
                    }

                    OutlinedTextField(
                        value = state.translation,
                        onValueChange = {
                            onAction(AddWordsActions.OnTranslationChange(it))
                        },
                        label = { Text(stringResource(R.string.translation_button)) },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                            .fillMaxWidth(),
                        isError = translationInputError != null,
                        supportingText = {
                            if (translationInputError != null) {
                                Text(
                                    text = "*$translationInputError",
                                    color = Color.Red
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),

                        )
                    Surface(
                        shape = RoundedCornerShape(15.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.cardsicon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = stringResource(R.string.homepage_button),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Switch(
                                checked = state.isOnHomePage,
                                onCheckedChange = {
                                    onAction(AddWordsActions.SetIsOnHomepage)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = MaterialTheme.colorScheme.secondary
                                )


                            )
                        }
                    }


                    MyAppButton(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                        text = stringResource(R.string.add),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        onClick = {
                            onAction(AddWordsActions.OnAddWordToList(
                                state.word, state.pronunciation, state.translation, state.isOnHomePage
                            ))

                            //Puts the cursor to the first Outlined Text field
//                        focusRequester.requestFocus()
                        }
                    )

                    TextButton(
                        onClick = dropUnlessResumed { onNavigateBack() },
                        text = stringResource(R.string.back_to_home)
                    )


                }
            }

            if (state.showWordInLibraryDialog) {
                WordInLibraryAlertDialog(
                    onDismiss = {
                        onAction(AddWordsActions.OnDismissDialog)
                    },
                    onConfirm = {
                        onAction(AddWordsActions.OnAddWordsToListAndFirebase(
                            state.word, state.pronunciation, state.translation, state.isOnHomePage
                        ))
                    },
                    currentLanguage = currentLanguage,
                    word1 = Words(
                        word = state.word,
                        translation = state.translation,
                        pronunciation = state.pronunciation,
                    ),

                    word2 = Words(
                        word = state.existingWordInLibrary?.word ?: "",
                        translation = state.existingWordInLibrary?.translation ?: "",
                        pronunciation = state.existingWordInLibrary?.pronunciation ?: "",
                    )

                )
            }
        }
    }

@Preview
@Composable
private fun Preview() {
    MyLanguageLearningAppTheme(
        appColorTheme = AppColorTheme.SUNSET
    ) {
        AddWords(
            snackbarHostState = SnackbarHostState(),
            state = AddWordsUiState(
                word = "Testing"
            ),
            currentLanguage = "jp"
        )
    }
}
