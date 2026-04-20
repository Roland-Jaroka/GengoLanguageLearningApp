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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.BottomNavBar
import com.example.gengolearning.ui.components.global.OpeningLoadingScreen
import com.example.gengolearning.ui.components.onBoardongRoot
import com.example.gengolearning.ui.features.autchentication.forgotpassword.ForgotPasswordScr
import com.example.gengolearning.ui.features.autchentication.login.LoginUi
import com.example.gengolearning.ui.features.autchentication.signup.SignUpUi
import com.example.gengolearning.ui.features.dashboard.home.Home
import com.example.gengolearning.ui.features.dashboard.home.addwords.AddWordsUi
import com.example.gengolearning.ui.features.dashboard.home.apiwords.ApiWordsScreen
import com.example.gengolearning.ui.features.dashboard.home.drawingquiz.DrawingQuizView
import com.example.gengolearning.ui.features.dashboard.home.mainlanguage.MainLanguageSelector
import com.example.gengolearning.ui.features.dashboard.home.mylist.MyListUi
import com.example.gengolearning.ui.features.dashboard.home.mylist.editcategory.EditCategory
import com.example.gengolearning.ui.features.dashboard.home.mylist.editword.EditWord
import com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory.MakeNewCategoryScreen
import com.example.gengolearning.ui.features.dashboard.home.quiz.QuizUi
import com.example.gengolearning.ui.features.dashboard.learning.AddNewGrammarUi
import com.example.gengolearning.ui.features.dashboard.learning.LearningUi
import com.example.gengolearning.ui.features.dashboard.learning.grammarDetails.GrammarDetails
import com.example.gengolearning.ui.features.dashboard.settings.LearningLanguageUi
import com.example.gengolearning.ui.features.dashboard.settings.Profile.ProfileMenu
import com.example.gengolearning.ui.features.dashboard.settings.settingsUi
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val startDestination = if (currentUser != null) Route.Dashboard else Route.Authentication
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.hasRoute<Route.Home>() ||
                it.hasRoute<Route.GrammarList>() ||
                it.hasRoute<Route.Settings>()
    } == true

    val context = LocalContext.current
    val isLoginDone by  AppSettingsPreferences.loginIsDone(context).collectAsState(null)

    LaunchedEffect(isLoginDone) {
        println("Login was done $isLoginDone")
    }

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

        if (isLoginDone != null) {

            println("Login screen is Login done = $isLoginDone")

        NavHost(
            navController= navController,
            startDestination= startDestination,
            modifier= Modifier
                .fillMaxSize()

        ) {

            //AuthFlow
            navigation<Route.Authentication>(
                startDestination = Route.Login
            ) {
                composable<Route.Login>(
                    exitTransition ={ slideOutHorizontally(animationSpec = tween(durationMillis = 1000)){fullWidth -> -fullWidth} } ) { LoginUi(navController = navController) }
                composable<Route.ForgotPassword> { ForgotPasswordScr(navController = navController) }
                composable<Route.SignUp> { SignUpUi(navController = navController) }

            }




               //DashboardFlow
               navigation<Route.Dashboard>(
                   startDestination = if (isLoginDone == true ) Route.Home else Route.MainLanguageSelector
               ) {

                   composable<Route.MainLanguageSelector>(
                       enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth -> fullWidth } },
                       exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth -> -fullWidth } }) {
                       MainLanguageSelector(
                           navController = navController
                       )
                   }

                   composable<Route.OnBoarding> { onBoardongRoot(navController = navController) }

                   composable<Route.Home> { Home(navController = navController) }


                   composable<Route.AddWords>(
                       enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth -> fullWidth } },
                       exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth -> fullWidth } },
                   ) { backStackEntry ->

                       backStackEntry.toRoute<Route.AddWords>()

                       AddWordsUi(navController = navController)
                   }
                   composable<Route.MyList>(
                       enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth -> fullWidth } },
                       exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth -> fullWidth } }) {
                       MyListUi(
                           navController = navController
                       )
                   }

                   composable<Route.EditWord> { backStackEntry ->

                       val route = backStackEntry.toRoute<Route.EditWord>()

                       EditWord(navController = navController, route.wordId)
                   }

                   composable<Route.NewCategory> {
                       MakeNewCategoryScreen(navController = navController)
                   }

                   composable<Route.EditCategory> { backStackEntry ->


                       val route = backStackEntry.toRoute<Route.EditCategory>()

                       EditCategory(navController = navController, categoryId = route.categoryId)
                   }

                   composable<Route.Quiz> { QuizUi(navController = navController) }

                   composable<Route.DrawingQuiz> { DrawingQuizView(navController = navController) }

                   composable<Route.GrammarList> { LearningUi(navController = navController) }

                   composable<Route.GrammarDetails>()
                   { backStackEntry ->

                       val route = backStackEntry.toRoute<Route.GrammarDetails>()

                       GrammarDetails(navController = navController, route.grammarId)
                   }

                   composable<Route.AddNewGrammar> { AddNewGrammarUi(navController = navController) }

                   composable<Route.Settings> { settingsUi(navController = navController) }
                   composable<Route.Profile> { ProfileMenu(navController = navController) }
                   composable<Route.LearningLanguage> { LearningLanguageUi(navController = navController) }
                   composable<Route.Dictionary> { ApiWordsScreen(navController = navController) }
               }

           }

        } else {
            OpeningLoadingScreen()
        }
    }
}
