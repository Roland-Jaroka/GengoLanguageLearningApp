package com.example.gengolearning.uielements.dashboard.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.AnalyticsHelper
import com.example.gengolearning.model.Languages
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningLanguageUi(viewModel: LearningLanguageViewModel= hiltViewModel(),
                       navController: NavController) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val mainLanguage by viewModel.mainLanguage.collectAsState("jp")
    var selectedMainLanguage by remember{ mutableStateOf("")}
    var selectedLanguage by remember { mutableStateOf(currentLanguage.code) }
    val scrollState = rememberScrollState()
    val languages = Languages.languagesList


    LaunchedEffect(mainLanguage) {
        selectedMainLanguage = mainLanguage
    }


    Scaffold( modifier = Modifier
        .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.learning_language))
                },
                navigationIcon = {
                    IconButton({
                        navController.popBackStack()
                    }) {
                        Image(
                            painter = painterResource(R.drawable.arrow_back2),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(White)

            )
        }

        ) { innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(White)
            .verticalScroll(scrollState)) {
            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .padding(12.dp),
                colors = CardDefaults.cardColors(White),
                border = BorderStroke(2.dp, Blue),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_langugae),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp),
                    fontSize = 20.sp
                )

                languages.forEach { language ->

                    Row(modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                        verticalAlignment =  Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {

                        Text(
                            text = language.name,
                            modifier = Modifier
                                .padding(start = 50.dp)
                                .weight(1f)
                        )
                        Checkbox(
                            checked = selectedLanguage == language.code,
                            onCheckedChange = {
                                if (it) selectedLanguage = language.code

                            },
                            modifier = Modifier
                                .padding(end = 20.dp)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.setLanguage(selectedLanguage)
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            restoreState = true


                        }
                        AnalyticsHelper.logEvent("learning_language_changed")
                        AnalyticsHelper.logEvent("selected_language_${selectedLanguage}")

                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.select_button)
                    )
                }

            }

            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .padding(12.dp),
                colors = CardDefaults.cardColors(White),
                border = BorderStroke(2.dp, Blue),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_main_language),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp),
                    fontSize = 20.sp
                )

               languages.forEach { language ->

                   Row(verticalAlignment =  Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center) {

                       Text(
                           text = language.name,
                           modifier = Modifier
                               .weight(1f)
                               .align(Alignment.CenterVertically)
                               .padding(start = 50.dp)
                       )
                       Checkbox(
                           checked = selectedMainLanguage == language.code,
                           onCheckedChange = {
                               if (it) selectedMainLanguage = language.code
                           },
                           modifier = Modifier
                               .align(Alignment.CenterVertically)
                               .padding(end = 20.dp)
                       )
                   }

                   HorizontalDivider(
                       modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                   )
               }


                Button(
                    onClick = {
                        viewModel.setMainLanguage(selectedMainLanguage)
                        navController.navigate("settings") {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            restoreState = true

                        }

                        AnalyticsHelper.logEvent("main_language_changed")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.select_button)
                    )
                }

            }
        }
    }
}
