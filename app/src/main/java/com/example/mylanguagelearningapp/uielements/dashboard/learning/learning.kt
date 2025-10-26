package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.GrammarCards

@Composable
fun LearningUi(navController: NavController,
               viewModel: LearningViewModel= viewModel()){
//TODO learning UI and functions

    val grammarList by viewModel.grammars.collectAsState()
    val currentLanguage by UserSettingsRepository.language.collectAsState()
    val searchInput = viewModel.search


    val grammars = remember(grammarList, searchInput) {
        if (searchInput.isBlank()) grammarList
        else grammarList.filter { grammar ->
            listOf(grammar.grammar, grammar.explanation, grammar.examples?.firstOrNull() ?: "").any{
                it.contains(searchInput, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
       viewModel.loadData(currentLanguage)
    }

    Box(modifier= Modifier
        .fillMaxSize()
        .background(White)
        .padding(bottom = 90.dp)){
        Column(modifier = Modifier
            .fillMaxSize()) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(BgBlue)) {

                Text(
                    text = "Grammars",
                    fontSize = 50.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
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

                    GrammarCards(
                        grammars.grammar,
                        grammars.explanation,
                        firstExample,
                        onClick = {
                            navController.navigate("grammarDetails/${grammars.id}")

                        })
                }
//                item {
//                    Text(text = "Migrate grammars",
//                        color= Blue,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth()
//                            .padding(top = 10.dp)
//                            .clickable(
//                                onClick = {
//                                    viewModel.migrateGrammar(currentLanguage)
//                                }
//                            ))
//                    Spacer(modifier = Modifier.height(50.dp))
//                }


            }


        }

    }


}