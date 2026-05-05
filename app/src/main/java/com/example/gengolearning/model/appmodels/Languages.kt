package com.example.gengolearning.model.appmodels

import com.gengolearning.app.R


data class Language(
    val code: String,
    val name: Int,
    val flag: Int,
    val englishName: String = ""
)






object Languages {

    val languagesList = listOf(
        Language("jp", R.string.jp, R.drawable.japanese, "Japanese"),
        Language("cn", R.string.cn, R.drawable.chinese, "Chinese"),
        Language("es", R.string.sp, R.drawable.spanish, "Spanish"),
        Language("en", R.string.en, R.drawable.english , "English"),
        Language("nw", R.string.norwegian, R.drawable.norwegianflagresized, "Norwegian"),
        Language("fr", R.string.fr, R.drawable.france, "French"),
        Language("de", R.string.gr, R.drawable.germany, "German"),
        Language("kr", R.string.korean, R.drawable.korea, "Korean"),
        Language ("sv", R.string.sw,R.drawable.sweden, "Swedish"),
        Language("it", R.string.it, R.drawable.italy, "Italian" )
    )
}
