package com.example.gengolearning.model.appmodels

import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.features.navigation.Route
import com.gengolearning.app.R

object DashboardFeaturesList {

    val list = listOf(
        DashboardFeatures(
            id = "Add new words",
            type = FeatureType.NewWords,
            icon = R.drawable.writing_icon,
            title = R.string.add_words_title,
            buttonText = R.string.add_words_button,
            buttonIcon = R.drawable.plus_icon,
            buttonSize = 18.dp,
            route = Route.AddWords(word = null, pronunciation = null, translation = null),
            supportedLanguages = null),
        DashboardFeatures(
            id = "My list",
            type = FeatureType.MyList,
            icon = R.drawable.mylist_book,
            title = R.string.my_list_title,
            buttonText = R.string.my_list_button,
            buttonIcon = R.drawable.list_icon,
            route = Route.MyList,
            supportedLanguages = null),
        DashboardFeatures(
            id = "Quizzes",
            type = FeatureType.Quizzes,
            icon = R.drawable.quizzes ,
            title = R.string.quizes_button,
            buttonText = R.string.quizes_button,
            buttonIcon = R.drawable.quiz_icon,
            route = Route.Quiz,
            supportedLanguages = null),
        DashboardFeatures(
            id = "Drawing quiz",
            type = FeatureType.DrawingQuiz,
            icon = R.drawable.caligraphy2,
            title = R.string.drawing_quiz_button,
            buttonText = R.string.drawing_quiz_button_title,
            buttonIcon = R.drawable.paintingbrush,
            buttonSize = 25.dp,
            route = Route.DrawingQuiz,
            supportedLanguages =setOf(
                Languages.languagesList[0],
                Languages.languagesList[1]
            ) ),
        DashboardFeatures(
            id = "Search",
            type = FeatureType.Dictionary,
            icon = R.drawable.open_dictionary,
            title = R.string.dictionary_title,
            buttonText = R.string.search,
            buttonIcon = R.drawable.search,
            route = Route.Dictionary,
            supportedLanguages = setOf(
                Languages.languagesList[0]
            )),
        DashboardFeatures(
            id = "LanguageChange",
            type = FeatureType.LanguageChange,
            icon = R.drawable.languages,
            title =R.string.select_language_title,
            buttonText = R.string.add_words_button,
            buttonIcon = R.drawable.quiz_icon,
            route = Route.LearningLanguage,
            supportedLanguages = null
        ),

        DashboardFeatures(
            id = "AiQuiz",
            type = FeatureType.AiQuiz,
            icon = R.drawable.learning_icon,
            title = R.string.aiQuiz_feature_title,
            buttonText = R.string.aiQuiz_feature_button,
            buttonIcon = R.drawable.quiz_icon,
            route = Route.AiQuiz,
            supportedLanguages = null
        )
    )
}