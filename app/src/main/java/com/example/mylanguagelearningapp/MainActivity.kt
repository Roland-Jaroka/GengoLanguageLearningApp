package com.example.mylanguagelearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.navigation.AppNavigation
import com.example.mylanguagelearningapp.ui.theme.MyLanguageLearningAppTheme
import com.example.mylanguagelearningapp.words.LanguageWords
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            UserSettingsRepository.loadMainLanguage(this@MainActivity)
        }


            setContent {

                MyLanguageLearningAppTheme {

                        AppNavigation()


                }
            }
        }
}
