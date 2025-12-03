package com.example.gengolearning.uielements.dashboard.home.mylist


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.Languages
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.example.gengolearning.uielements.uimodels.TextButton
import com.gengolearning.app.R

@Composable
fun EditWord(
    navController: NavController,
    wordId: String?,
    viewModel: EditWordViewModel = hiltViewModel()
) {
  val word = viewModel.word
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Word",
                fontSize = 30.sp,
                color = Blue
            )
            Text(
                text = "Edit the selected word",
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            OutlinedTextField(
                value = word!!.word,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                label = { Text(stringResource(R.string.word_button)) }
            )

            if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {

                OutlinedTextField(
                    value = word.pronunciation,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    label = { Text(stringResource(R.string.pronuncitaon_button)) }
                )
            }

            OutlinedTextField(
                value = word.translation,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                label = { Text(stringResource(R.string.translation_button)) }
            )

            MyAppButton(
                onClick = {},
                text = "Edit",
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                )
            )

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                text = "Back to My list"
            )

        }
    }
}