package com.example.gengolearning.uielements.dashboard.home.addwords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.ErrorModal
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.example.gengolearning.uielements.uimodels.TextButton
import com.gengolearning.app.R

@Composable
fun AddWordsUi(navController: NavController,
               viewModel: AddWordsViewModel = viewModel()) {

    val word = viewModel.word
    val translation = viewModel.translation
    val pronunciation = viewModel.pronunciation
    val scrollstate= rememberScrollState()
    val currentLanguage by UserSettingsRepository.language.collectAsState()

    val wordInputError = viewModel.wordInputError?.let {id-> stringResource(id)  }
    val translationInputError = viewModel.translationInputError?.let { id->
        stringResource(id)
    }
    val error = viewModel.error


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
                    .fillMaxWidth(),
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
                }
            )

            TextButton(
                onClick = {
                    navController.navigate("home")
                },
                text = stringResource(R.string.back_to_home)
            )


        }
    }
}