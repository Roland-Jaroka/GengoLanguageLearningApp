package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.R
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar

@Composable
fun LearningUi(navController: NavController,
               viewModel: LearningViewModel= viewModel()){
//TODO learning UI and functions

    val grammars = viewModel.filteredGrammar

    LaunchedEffect(Unit) {
        JapaneseGrammar.loadGrammar()
    }

    Box(modifier= Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text(
                text = "Grammar",
                fontSize = 30.sp,
                color = Blue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Row() {
                OutlinedTextField(
                    value = viewModel.search,
                    onValueChange = {viewModel.onSearchValueChange(it)},
                    label = { Text("Search") },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_search),
                            contentDescription = null
                        )
                    }
                )
                Image(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                        .padding(top = 20.dp)
                        .clickable{
                            navController.navigate("addnewgrammar")
                        }
                )
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(grammars){grammars ->

                    val firstExample = grammars.examples?.firstOrNull() ?: ""

                    GrammarCards(grammars.grammar,
                        grammars.explanation,
                        firstExample,
                        onClick = {
                            navController.navigate("grammarDetails/${grammars.id}")

                        })
                }


            }


        }

    }


}