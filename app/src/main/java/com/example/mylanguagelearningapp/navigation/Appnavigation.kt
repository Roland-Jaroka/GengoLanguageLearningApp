package com.example.mylanguagelearningapp.navigation

import android.view.WindowInsets.Type.statusBars
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mylanguagelearningapp.model.BottomNavBar
import com.example.mylanguagelearningapp.uielements.autchentication.forgotpassword.ForgotPasswordScr
import com.example.mylanguagelearningapp.uielements.autchentication.login.LoginUi
import com.example.mylanguagelearningapp.uielements.autchentication.signup.SignUpUi
import com.example.mylanguagelearningapp.uielements.dashboard.home.Home
import com.example.mylanguagelearningapp.uielements.dashboard.home.addwords.AddWordsUi
import com.example.mylanguagelearningapp.uielements.dashboard.home.drawingquiz.DrawingQuizView
import com.example.mylanguagelearningapp.uielements.dashboard.home.mylist.MyListUi
import com.example.mylanguagelearningapp.uielements.dashboard.home.quiz.QuizUi
import com.example.mylanguagelearningapp.uielements.dashboard.learning.AddNewGrammarUi
import com.example.mylanguagelearningapp.uielements.dashboard.learning.GrammarDetails
import com.example.mylanguagelearningapp.uielements.dashboard.learning.LearningUi
import com.example.mylanguagelearningapp.uielements.dashboard.settings.LearningLanguageUi
import com.example.mylanguagelearningapp.uielements.dashboard.settings.settingsUi
import com.google.firebase.auth.FirebaseAuth



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

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

    ) {innerPadding ->


        NavHost(
            navController= navController,
            startDestination= startDestination,
            modifier= Modifier
                .fillMaxSize()
                .padding(
                    bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp
                )

        ) {

            //AuthFlow
            navigation(
                startDestination = "login",
                route = "authentication"
            ) {
                composable("login") { LoginUi(navController = navController) }
                composable("forgot_password") { ForgotPasswordScr(navController = navController) }
                composable("signup") { SignUpUi(navController = navController) }

            }


            //DashboardFlow
            navigation(
                startDestination = "home",
                route = "dashboard"
            ) {
                composable("home") { Home(navController = navController) }

                composable("addwords") { AddWordsUi(navController = navController) }

                composable("mylist") { MyListUi(navController = navController) }

                composable("quiz") { QuizUi(navController= navController) }

                composable("drawing") { DrawingQuizView(navController= navController) }

                composable("learning") { LearningUi(navController= navController) }

                composable("grammarDetails/{grammarId}",
                    arguments = listOf(navArgument("grammarId") { type = NavType.StringType }) )
                { backStackEntry ->

                    val grammarId = backStackEntry.arguments?.getString("grammarId")

                    GrammarDetails(navController = navController, grammarId) }

                composable("addnewgrammar") { AddNewGrammarUi(navController = navController) }

                composable("settings") { settingsUi(navController = navController) }
                composable("learningLanguage") { LearningLanguageUi() }
            }
        }
    }
}
