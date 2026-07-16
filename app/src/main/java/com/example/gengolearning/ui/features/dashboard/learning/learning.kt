package com.example.gengolearning.ui.features.dashboard.learning

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.components.AddButton
import com.example.gengolearning.ui.components.GrammarCards
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.global.SearchBar
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.PandaBlack
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun LearningUi(navController: NavController,
               viewModel: LearningViewModel= hiltViewModel()){


    val grammarList by viewModel.grammar.collectAsState()
    val searchInput = viewModel.search


    val grammars = remember(grammarList, searchInput) {
        if (searchInput.isBlank()) grammarList
        else grammarList.filter { grammar ->
            listOf(grammar.grammar, grammar.explanation, grammar.examples?.firstOrNull() ?: "").any{
                it.contains(searchInput, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .statusBarsPadding()
        ) {

        Text(
            text = stringResource(R.string.grammar),
            fontSize = 50.sp,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 90.dp),
            shadowElevation = 10.dp
        ) {
            Column( modifier = Modifier
                .padding(top = 18.dp)) {

            Row(
                modifier = Modifier
                    .padding(18.dp)
            ) {

                SearchBar(
                    searchInput = viewModel.search,
                    onValueChange = {
                        viewModel.onSearchValueChange(it)
                    },
                    modifier = Modifier.weight(1f),
                    onClear = {
                        viewModel.onSearchValueChange("")
                    },
                    hasLabel = true
                )

                AddButton(
                    onClick = {
                        navController.navigate(Route.AddNewGrammar)

                        AnalyticsHelper.logEvent("add_grammar_button_plus")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(40.dp)
                        .padding(start = 2.dp)
                )
            }

                // empty list view:

                if (grammarList.isEmpty() || grammars.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Image(
                            painter = painterResource(R.drawable.emptybox),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(80.dp)
                        )

                        androidx.wear.compose.material.Text(
                            text = if (grammarList.isEmpty()) stringResource(R.string.empty_grammar_list) else stringResource(
                                R.string.grammar_list_empty_search
                            ),
                            fontSize = 20.sp,
                            color = PandaBlack,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally)
                        )


                        MyAppButton(
                            onClick = {
                                navController.navigate(Route.AddNewGrammar)
                                AnalyticsHelper.logEvent("add_grammar_button")
                            },
                            text = stringResource(R.string.add),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)

                        )


                    }
                } else

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(grammars) { grammars ->

                    val firstExample = grammars.examples?.firstOrNull() ?: ""

                    GrammarCards(
                        grammars.grammar,
                        grammars.explanation,
                        firstExample,
                        onClick = {
                            navController.navigate(Route.GrammarDetails(grammars.id))
                            AnalyticsHelper.logEvent("grammar_Details")

                        })
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }


            }
        }


        }


    }



}