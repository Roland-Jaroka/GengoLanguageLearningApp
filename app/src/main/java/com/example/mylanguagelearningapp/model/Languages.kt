package com.example.mylanguagelearningapp.model

import com.example.mylanguagelearningapp.R

data class Language(
    val code: String,
    val name: String,
    val flag: Int
)






object Languages {

    val languagesList = listOf(
        Language("jp", "Japanese", R.drawable.japanese),
        Language("cn", "Chinese", R.drawable.chinese),
        Language("es", "Spanish", R.drawable.spanish),
        Language("en", "English", R.drawable.english),
        Language("nw", "Norwegian", R.drawable.norwegian),
        Language("fr", "French", R.drawable.france),
        Language("de", "German", R.drawable.germany),
        Language("kr", "Korean", R.drawable.korea)
    )
}
