package com.example.gengolearning.model.appmodels

import com.gengolearning.app.R

data class AppLanguage(
    val languageTag: String,
    val displayName: Int
)
object AppLanguages {

    val languages = listOf(
        AppLanguage("en", R.string.en),
        AppLanguage("ja", R.string.jp),
        AppLanguage("hu", R.string.hu))

}