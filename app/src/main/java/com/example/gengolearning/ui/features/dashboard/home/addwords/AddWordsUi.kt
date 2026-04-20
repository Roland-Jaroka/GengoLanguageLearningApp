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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.gengolearning.app.R

@Composable
fun AddWordsUi(navController: NavController,
               viewModel: AddWordsViewModel = hiltViewModel()) {

    val word = viewModel.word
    val translation = viewModel.translation
    val pronunciation = viewModel.pronunciation
    val existingWordInLibrary = viewModel.existingWordInLibrary
    val scrollstate= rememberScrollState()
    val currentLanguage = viewModel.currentLanguage

    val wordInputError = viewModel.wordInputError?.let {id-> stringResource(id)  }
    val translationInputError = viewModel.translationInputError?.let { id->
        stringResource(id)
    }
    val error = viewModel.error
    val focusRequester = remember { FocusRequester() }

    val checked = viewModel.isOnHomePage

    val showWordInLibraryDialog = viewModel.showWordInLibraryDialog





    Box(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollstate)) {

        Column(modifier = Modifier.align(Alignment.Center)) {

            Text(
                text = stringResource(R.string.add_words_button),
                fontSize = 30.sp,
                color = Blue,
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
                value = word,
                onValueChange = { viewModel.onWordChange(newWord = it)
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



            if(currentLanguage == "jp" || currentLanguage == "cn") {
                OutlinedTextField(
                    value = pronunciation,
                    onValueChange = {
                        viewModel.onPronunciationChange(it)
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
                value = translation,
                onValueChange = { viewModel.onTranslationChange(it)
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
            Surface (
                shape = RoundedCornerShape(15.dp),
                color = White,
                shadowElevation = 2.dp ,
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
                        checked = checked,
                        onCheckedChange = {
                            viewModel.setIsOnHomePage()
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Blue
                        )


                    )
                }
            }


            MyAppButton(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                text= stringResource(R.string.add),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                onClick = {
                    viewModel.addWordToList()

                    //Puts the cursor to the first Outlined Text field
                    focusRequester.requestFocus()
                }
            )

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                text = stringResource(R.string.back_to_home)
            )


        }
    }

    if (showWordInLibraryDialog) {
        WordInLibraryAlertDialog(
            onDismiss = {
                viewModel.onDismissDialog()
            },
            onConfirm = {
                viewModel.addWordToListAndFirebase()
            },
            currentLanguage = currentLanguage,
            word1 = Words(
                word = viewModel.word,
                translation = viewModel.translation,
                pronunciation = viewModel.pronunciation,
            ),

            word2 = Words(
                word = existingWordInLibrary?.word ?: "",
                translation = existingWordInLibrary?.translation ?:"",
                pronunciation = existingWordInLibrary?.pronunciation ?: "",
            )

        )
    }
}