package com.example.gengolearning.ui.features.dashboard.home.aiquiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.gengolearning.ui.features.dashboard.home.aiquiz.AiQuizActions
import com.example.gengolearning.ui.features.dashboard.home.aiquiz.AiQuizModals
import com.example.gengolearning.ui.features.dashboard.home.aiquiz.AiQuizViewmodel
import com.gengolearning.app.R


@Composable
fun AiQuizUiRoot(
    viewmodel: AiQuizViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

   when  {
       state.modals is AiQuizModals.UnknownError -> AiQuizErrorScreen(
           onTryAgain = {
               viewmodel.onAction(AiQuizActions.onRetry)
           },
           onBack = {
               navController.popBackStack()
           },
           title = stringResource( R.string.oops),
           text = stringResource(R.string.common_error_server),
           buttonText = stringResource(R.string.common_try_again),
           backButtonText = stringResource(R.string.common_go_back)
       )

       state.modals is AiQuizModals.ServerError -> AiQuizErrorScreen(
           onTryAgain = {
               viewmodel.onAction(AiQuizActions.onRetry)
           },
           onBack = {
               navController.popBackStack()
           },
           title = stringResource( R.string.oops),
           text = stringResource(R.string.ai_quiz_server_error),
           buttonText = stringResource(R.string.common_try_again),
           backButtonText = stringResource(R.string.common_go_back)
       )

       state.modals is AiQuizModals.NoInternet -> NoInternetErrorScreen(
           onTryAgain = {
               viewmodel.onAction(AiQuizActions.onRetry)
           },
           onBack = {
               navController.popBackStack()
           }
       )

       state.isLoading -> AiQuizLoadingScreen()

       else ->   AiQuizUI(
           state = state,
           onAction = viewmodel::onAction,
           onBackClick = {
               navController.popBackStack()
           },
           currentLanguage = state.currentLanguage
       )
   }

}

