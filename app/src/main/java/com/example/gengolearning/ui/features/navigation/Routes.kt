package com.example.gengolearning.ui.features.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Authentication: Route

    @Serializable
    data object Login: Route
    @Serializable
    data object SignUp: Route
    @Serializable
    data object ForgotPassword: Route
    @Serializable
    data object MainLanguageSelector: Route

    @Serializable
    data object OnBoarding: Route

    @Serializable
    data object Dashboard: Route
    @Serializable
    data object Home: Route
    @Serializable
    data class AddWords(val word: String?, val pronunciation: String?, val translation: String?): Route
    @Serializable
    data object MyList: Route

    @Serializable
    data class EditWord(val wordId: String?): Route

    @Serializable
    data class EditCategory(val categoryId: String?): Route

    @Serializable
    data object Quiz: Route

    @Serializable
    data object DrawingQuiz: Route

    @Serializable
    data object Dictionary: Route

    @Serializable
    data object LearningLanguage: Route

    @Serializable
    data object NewCategory: Route

    @Serializable
    data object GrammarList: Route

    @Serializable
    data object AddNewGrammar: Route

    @Serializable
    data class GrammarDetails(val grammarId: String): Route


    @Serializable
    data object Settings: Route

    @Serializable
    data object Profile: Route
}
