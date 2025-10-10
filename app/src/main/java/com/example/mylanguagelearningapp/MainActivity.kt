package com.example.mylanguagelearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mylanguagelearningapp.model.UserSettingsRepository

import com.example.mylanguagelearningapp.navigation.AppNavigation
import com.example.mylanguagelearningapp.ui.theme.MyLanguageLearningAppTheme
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            lifecycleScope.launch {
                UserSettingsRepository.getUserData()
                val mainLang = UserSettingsRepository.mainLanguage.value

                when (mainLang) {
                    "jp" -> {
                        JapaneseWords.loadWords()
                    }

                    "cn" -> {
                        ChineseWords.loadWords()
                    }
                }
            }


        } else {

        }
            setContent {

                MyLanguageLearningAppTheme {

                    val dataLoaded by UserSettingsRepository.dataLoaded.collectAsState()

                    val firstLogin = UserSettingsRepository.firstLogin.value

                    AppNavigation(firstLogin == true)

                }
            }
        }
}
