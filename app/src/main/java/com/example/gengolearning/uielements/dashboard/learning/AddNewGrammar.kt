package com.example.gengolearning.uielements.dashboard.learning

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.AddButton
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.example.gengolearning.uielements.uimodels.TextButton
import com.gengolearning.app.R

@Composable
fun AddNewGrammarUi(navController: NavController,
                    viewModel: AddNewGrammarViewModel= hiltViewModel()
) {

    val examplerows = viewModel.examplerows
    val scrollState = rememberScrollState()

    val grammarInputError = viewModel.grammarInputError?.let { id->
        stringResource(id)
    }
    val explanationInputError = viewModel.explanationInputError?.let { id->
        stringResource(id)
    }
    val currentLanguage by viewModel.currentLanguage.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .align(Alignment.Center)
            .verticalScroll(scrollState)) {

            Text(
                text = stringResource(R.string.new_grammar),
                fontSize = 30.sp,
                color = Blue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.new_grammar_info),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )

            OutlinedTextField(
                value = viewModel.grammar,
                onValueChange = {
                    viewModel.onGrammarInputChange(it)
                },
                label = { Text(stringResource(R.string.grammar)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .fillMaxWidth(),
                singleLine = true,
                isError = grammarInputError != null,
                supportingText = {
                    if (grammarInputError != null) {
                        Text(
                            text = "*$grammarInputError",
                            color = Color.Red
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )

            OutlinedTextField(
                value = viewModel.explanation,
                onValueChange = {
                    viewModel.onExplanationInputChange(it)
                },
                label = { Text(stringResource(R.string.explanation),
                    modifier = Modifier.padding(top = 10.dp)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                isError = explanationInputError != null,
                supportingText = {
                    if (explanationInputError != null) {
                        Text(
                            text = "*$explanationInputError",
                            color = Color.Red
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )



            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 10.dp)
            ) {

                OutlinedTextField(
                    value = viewModel.example,
                    onValueChange = {
                        viewModel.onFirstExampleChange(it)
                    },
                    label = { Text(stringResource(R.string.examples),
                        modifier = Modifier.padding(top = 10.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp)
                )

                AddButton(
                    onClick = {
                        if (examplerows.size < 5)
                            examplerows.add("")
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                        .padding(start = 5.dp)
                )
            }

                examplerows.forEachIndexed { index, textValue ->

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 30.dp, end = 20.dp, top = 20.dp)
                    ) {

                        OutlinedTextField(
                            value = textValue,
                            onValueChange = {
                                newValue -> examplerows[index] = newValue
                            },
                            label = { Text(stringResource(R.string.examples)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(20.dp)
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
                    viewModel.addGrammarToList()
                }

            )

            TextButton(
                onClick = {
                    navController.navigate("learning")
                },
                text = stringResource(R.string.back_to_Grammar)
            )

        }
    }



}