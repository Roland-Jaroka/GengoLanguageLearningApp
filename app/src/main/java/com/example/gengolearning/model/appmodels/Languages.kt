package com.example.gengolearning.model.appmodels

import com.gengolearning.app.R


data class Language(
    val code: String,
    val name: Int,
    val flag: Int
)






object Languages {

    val languagesList = listOf(
        Language("jp", R.string.jp, R.drawable.japanese),
        Language("cn", R.string.cn, R.drawable.chinese),
        Language("es", R.string.sp, R.drawable.spanish),
        Language("en", R.string.en, R.drawable.english),
        Language("nw", R.string.norwegian, R.drawable.norwegianflagresized),
        Language("fr", R.string.fr, R.drawable.france),
        Language("de", R.string.gr, R.drawable.germany),
        Language("kr", R.string.korean, R.drawable.korea),
        Language ("sv", R.string.sw,R.drawable.sweden),
        Language("it", R.string.it, R.drawable.italy)
    )
}
