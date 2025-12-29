package com.example.gengolearning.ui.features.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gengolearning.model.BottomNavBar
import com.example.gengolearning.ui.features.autchentication.forgotpassword.ForgotPasswordScr
import com.example.gengolearning.ui.features.autchentication.login.LoginUi
import com.example.gengolearning.ui.features.autchentication.signup.SignUpUi
import com.example.gengolearning.ui.features.dashboard.home.ApiWordsScreen
import com.example.gengolearning.ui.features.dashboard.home.Home
import com.example.gengolearning.ui.features.dashboard.home.addwords.AddWordsUi
import com.example.gengolearning.ui.features.dashboard.home.drawingquiz.DrawingQuizView
import com.example.gengolearning.ui.features.dashboard.home.mainlanguage.MainLanguageSelector
import com.example.gengolearning.ui.features.dashboard.home.mylist.EditWord
import com.example.gengolearning.ui.features.dashboard.home.mylist.MyListUi
import com.example.gengolearning.ui.features.dashboard.home.quiz.QuizUi
import com.example.gengolearning.ui.features.dashboard.learning.AddNewGrammarUi
import com.example.gengolearning.ui.features.dashboard.learning.GrammarDetails
import com.example.gengolearning.ui.features.dashboard.learning.LearningUi
import com.example.gengolearning.ui.features.dashboard.settings.LearningLanguageUi
import com.example.gengolearning.ui.features.dashboard.settings.ProfileMenu
import com.example.gengolearning.ui.features.dashboard.settings.settingsUi
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUser = remember {auth.currentUser}

    val startDestination = if (currentUser != null) "dashboard" else "authentication"
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val showBottomBar = currentDestination in listOf("home","learning","settings")

    Scaffold (
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = 1000)

                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = 1000)
                )
            )

            { BottomNavBar(navController)}
        }

    ) {//innerPadding ->


        NavHost(
            navController= navController,
            startDestination= startDestination,
            modifier= Modifier
                .fillMaxSize()

        ) {

            //AuthFlow
            navigation(
                startDestination = "login",
                route = "authentication"
            ) {
                composable("login",
                    exitTransition ={ slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> -fullWidth} } ) { LoginUi(navController = navController) }
                composable("forgot_password") { ForgotPasswordScr(navController = navController) }
                composable("signup") { SignUpUi(navController = navController) }

            }

            composable("mainLanguageSelector",
                enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} },
                exitTransition ={ slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> -fullWidth} } ) { MainLanguageSelector(navController = navController) }


            //DashboardFlow
            navigation(
                startDestination = "home",
                route = "dashboard"
            ) {
                composable("home") { Home(navController = navController) }


                composable("addwords?word={word}&pronunciation={pronunciation}&translation={translation}",
                    arguments = listOf(
                        navArgument("word"){
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                        navArgument("pronunciation"){
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                        navArgument("translation") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    ),
                    enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} }) { AddWordsUi(navController = navController) }
                composable("mylist",
                    enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} }) { MyListUi(navController = navController) }

                 composable("editWord/{wordId}") { backStackEntry ->
                    val wordId = backStackEntry.arguments?.getString("wordId")
                     EditWord(navController = navController, wordId)  }

                composable("quiz") { QuizUi(navController= navController) }

                composable("drawing") { DrawingQuizView(navController= navController) }

                composable("learning") { LearningUi(navController= navController) }

                composable("grammarDetails/{grammarId}")
                { backStackEntry ->
                    val grammarId: String = backStackEntry.arguments?.getString("grammarId").toString()

                    GrammarDetails(navController = navController, grammarId) }

                composable("addnewgrammar") { AddNewGrammarUi(navController = navController) }

                composable("settings") { settingsUi(navController = navController) }
                composable("profile") { ProfileMenu(navController = navController) }
                composable("learningLanguage") { LearningLanguageUi(navController = navController) }
                composable("apiwords") { ApiWordsScreen(navController = navController) }
            }

        }
    }
}
