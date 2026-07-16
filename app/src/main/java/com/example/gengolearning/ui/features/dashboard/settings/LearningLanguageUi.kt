package com.example.gengolearning.ui.features.dashboard.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.components.LanguageSelectionRow
import com.example.gengolearning.ui.components.LoadingScreen
import com.example.gengolearning.ui.features.dashboard.home.mainlanguage.LanguageSelectorUiEvents
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningLanguageUi(viewModel: LearningLanguageViewModel= hiltViewModel(),
                       navController: NavController) {

    val scrollState = rememberScrollState()
    val languages = Languages.languagesList
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsStateWithLifecycle()



    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { events ->
            when (events) {
                LanguageSelectorUiEvents.NavigateToDashboard -> {
                    navController.navigate(Route.Home) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }

    }


    Scaffold( modifier = Modifier
        .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.learning_language))
                },
                navigationIcon = {
                    IconButton(onClick = dropUnlessResumed {
                        navController.popBackStack()
                    }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.arrow_back2),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background)

            )
        }

        ) { innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)) {
            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .animateContentSize()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {


                    Text(
                        text = stringResource(R.string.select_langugae),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(20.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // if the list is expanded it shows all the languages if its not it shows only
                    //the one that is the current language
                    val visibleLanguages = languages

                    visibleLanguages.forEach { language ->

                        LanguageSelectionRow(
                            flag = language.flag,
                            language = stringResource(language.name),
                            selected = state.selectedLanguage == language.code,
                            onSelect = {

                                viewModel.selectCurrentLanguage(language.code)
                                    viewModel.setLanguage(language.code)




                            }
                        )
                    }

                Spacer(modifier = Modifier.height(15.dp))

            }

            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .animateContentSize()
                    .width(300.dp)
                    .padding(12.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_main_language),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                languages.forEach { language ->

                    LanguageSelectionRow(
                        flag = language.flag,
                        language = stringResource(language.name),
                        selected = language.code == state.selectedMainLanguage,
                        onSelect = {

                            viewModel.selectMainLanguage(language.code)

                            scope.launch {
                                viewModel.setMainLanguage(language.code)
                            }

                            AnalyticsHelper.logEvent("main_language_changed")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))

            }
        }
    }

    if (state.isLoading) {
        LoadingScreen()
    }
}
