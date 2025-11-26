package com.example.gengolearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.gengolearning.model.AnalyticsHelper
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.navigation.AppNavigation
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.init(this)
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
