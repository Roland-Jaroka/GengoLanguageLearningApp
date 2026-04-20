package com.example.gengolearning.model.appmodels

import androidx.compose.ui.unit.Dp
import com.example.gengolearning.ui.features.navigation.Route

data class DashboardFeatures(
    val id: String,
    val type: FeatureType,
    val icon: Int,
    val title: Int,
    val buttonText: Int,
    val buttonIcon: Int,
    val buttonSize: Dp? = null,
    val route: Route,
    val supportedLanguages: Set<Language>? = null
)

enum class FeatureType {
    NewWords,
    MyList,
    Quizzes,
    DrawingQuiz,
    Dictionary,
    LanguageChange
}

