package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White

@Composable
fun AddNewGrammarUi(navController: NavController,
                    viewModel: AddNewGrammarViewModel= viewModel()) {

    val examplerows = viewModel.examplerows
    val scrollState = rememberScrollState()

    var grammarInputError by remember { mutableStateOf<String?>(null) }
    var explanationInputError by remember { mutableStateOf<String?>(null) }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .align(Alignment.Center)
            .verticalScroll(scrollState)) {

            Text(
                text = "New Grammar",
                fontSize = 30.sp,
                color = Blue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Add new grammar points to your list",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )

            OutlinedTextField(
                value = viewModel.grammar,
                onValueChange = {
                    viewModel.onGrammarInputChange(it)
                    grammarInputError = null
                },
                label = { Text("Grammar") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .fillMaxWidth(),
                singleLine = true,
                isError = grammarInputError != null,
                shape = RoundedCornerShape(20.dp)
            )

            if (grammarInputError != null) {
                Text(
                    text = "*$grammarInputError",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 30.dp, top = 5.dp)
                )
            }

            OutlinedTextField(
                value = viewModel.explanation,
                onValueChange = {
                    viewModel.onExplanationInputChange(it)
                    explanationInputError = null
                },
                label = { Text("Explanation",
                    modifier = Modifier.padding(top = 10.dp)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                isError = explanationInputError != null,
                shape = RoundedCornerShape(20.dp)
            )

            if (explanationInputError != null) {
                Text(
                    text = "*$explanationInputError",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 30.dp, top = 5.dp)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 10.dp, top = 20.dp)
            ) {

                OutlinedTextField(
                    value = viewModel.example,
                    onValueChange = {
                        viewModel.onFirstExampleChange(it)
                    },
                    label = { Text("Examples",
                        modifier = Modifier.padding(top = 10.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp)
                )
                Image(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .padding(start = 5.dp)
                        .clickable{
                            if (examplerows.size < 5)
                            examplerows.add("")}

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
                            label = { Text("Examples") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }


            Button(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                elevation = ButtonDefaults.buttonElevation(hoveredElevation = 10.dp, pressedElevation = 10.dp, defaultElevation = 5.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    when {
                        viewModel.grammar.isBlank() -> grammarInputError = "Grammar cannot be blank"
                        viewModel.explanation.isBlank() -> explanationInputError = "Explanation cannot be blank"
                        else -> viewModel.addGrammarToList()
                    }
                }
            )
            {

                Text(
                    text = "Add",
                    fontSize = 20.sp
                )
            }


            Text(
                text = "Back to Grammars",
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
                    .clickable(indication = null,
                        interactionSource = remember{ MutableInteractionSource() }) {

                        navController.navigate("learning")
                    },
                color = Blue)

        }
    }



}