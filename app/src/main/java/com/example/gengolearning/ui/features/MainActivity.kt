package com.example.gengolearning.ui.features

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.features.navigation.AppNavigation
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.init(this)
        enableEdgeToEdge()




                setContent {
                val themeViewmodel: ThemeViewModel = hiltViewModel()
                val appTheme by themeViewmodel.theme.collectAsStateWithLifecycle()

                    MyLanguageLearningAppTheme(appColorTheme = appTheme) {


                        AppNavigation(colorTheme = appTheme)


                    }
                }

        }
}
