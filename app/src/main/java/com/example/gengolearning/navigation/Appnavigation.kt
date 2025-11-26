package com.example.gengolearning.navigation

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
import com.example.gengolearning.uielements.autchentication.forgotpassword.ForgotPasswordScr
import com.example.gengolearning.uielements.autchentication.login.LoginUi
import com.example.gengolearning.uielements.autchentication.signup.SignUpUi
import com.example.gengolearning.uielements.dashboard.home.Home
import com.example.gengolearning.uielements.dashboard.home.addwords.AddWordsUi
import com.example.gengolearning.uielements.dashboard.home.drawingquiz.DrawingQuizView
import com.example.gengolearning.uielements.dashboard.home.mainlanguage.MainLanguageSelector
import com.example.gengolearning.uielements.dashboard.home.mylist.MyListUi
import com.example.gengolearning.uielements.dashboard.home.quiz.QuizUi
import com.example.gengolearning.uielements.dashboard.learning.AddNewGrammarUi
import com.example.gengolearning.uielements.dashboard.learning.GrammarDetails
import com.example.gengolearning.uielements.dashboard.learning.LearningUi
import com.example.gengolearning.uielements.dashboard.settings.LearningLanguageUi
import com.example.gengolearning.uielements.dashboard.settings.ProfileMenu
import com.example.gengolearning.uielements.dashboard.settings.settingsUi
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


                composable("addwords",
                    enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} }) { AddWordsUi(navController = navController) }

                composable("mylist",
                    enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> fullWidth} }) { MyListUi(navController = navController) }

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
                composable("profile") { ProfileMenu(navController = navController) }
                composable("learningLanguage") { LearningLanguageUi(navController = navController) }
            }

        }
    }
}
