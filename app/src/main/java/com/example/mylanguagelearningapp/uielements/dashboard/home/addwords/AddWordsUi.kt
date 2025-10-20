package com.example.mylanguagelearningapp.uielements.dashboard.home.addwords

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.model.results.AddWordResults
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton
import com.example.mylanguagelearningapp.words.JapaneseWords

@Composable
fun AddWordsUi(navController: NavController,
               viewModel: AddWordsViewModel = viewModel()) {

    val word = viewModel.word
    val translation = viewModel.translation
    val pronunciation = viewModel.pronunciation
    val context= LocalContext.current
    val scrollstate= rememberScrollState()
    val currentLanguage by UserSettingsRepository.language.collectAsState()

    var wordInputError by remember { mutableStateOf<String?>(null) }
    var translationInputError by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollstate)) {

        Column(modifier = Modifier.align(Alignment.Center)) {

            Text(
                text = "New Words",
                fontSize = 30.sp,
                color = Blue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Add new words to your list",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )

            OutlinedTextField(
                value = word,
                onValueChange = { viewModel.onWordChange(newWord = it)
                },
                label = { Text("Word") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .fillMaxWidth(),
                singleLine = true,
                isError = wordInputError != null,
                shape = RoundedCornerShape(20.dp),

            )

            if (wordInputError != null) {
                Text(
                    text = "*$wordInputError",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 30.dp, top = 5.dp)
                )
            }

            if(currentLanguage == "jp" || currentLanguage == "cn") {
                OutlinedTextField(
                    value = pronunciation,
                    onValueChange = {
                        viewModel.onPronunciationChange(it)
                    },
                    label = { Text("Pronunciations") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                )
            }

            OutlinedTextField(
                value = translation,
                onValueChange = { viewModel.onTranslationChange(it)
                },
                label = { Text("Translation") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .fillMaxWidth(),
                isError = translationInputError != null,
                singleLine = true,
                shape = RoundedCornerShape(20.dp),

                )

            if (translationInputError != null) {
                Text(
                    text = "*$translationInputError",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 30.dp, top = 5.dp)
                )
            }
            MyAppButton(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                text= "Add",
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                onClick = {

                    val result = viewModel.addWordToList()

                    when (result) {
                        is AddWordResults.BlankWord -> wordInputError = "Word cannot be blank"
                        is AddWordResults.BlankTranslation -> translationInputError = "Translation cannot be blank"
                        is AddWordResults.Success -> {

                        }
                        is AddWordResults.Error -> Toast.makeText(context,
                            JapaneseWords.error, Toast.LENGTH_SHORT).show()
                    }


                }
            )


                Text(
                text = "Back to home",
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top= 10.dp)
                    .clickable(indication = null,
                        interactionSource = remember{ MutableInteractionSource() }) {

                        navController.navigate("home")
                    },
                color = Blue)
        }
    }
}