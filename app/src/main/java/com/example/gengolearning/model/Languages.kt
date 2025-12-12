package com.example.gengolearning.model

import com.gengolearning.app.R


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
        Language("nw", "Norwegian", R.drawable.norwegianflagresized),
        Language("fr", "French", R.drawable.france),
        Language("de", "German", R.drawable.germany),
        Language("kr", "Korean", R.drawable.korea),
        Language ("sv", "Swedish",R.drawable.sweden),
        Language("it", "Italian", R.drawable.italy)
    )
}
