package com.example.mylanguagelearningapp.model

object Tonemarks {
    val tonemap= mapOf(
        "a1" to "ā", "a2" to "á", "a3" to "ǎ", "a4" to "à",
        "e1" to "ē", "e2" to "é", "e3" to "ě", "e4" to "è",
        "i1" to "ī", "i2" to "í", "i3" to "ǐ", "i4" to "ì",
        "o1" to "ō", "o2" to "ó", "o3" to "ǒ", "o4" to "ò",
        "u1" to "ū", "u2" to "ú", "u3" to "ǔ", "u4" to "ù",
        "ü1" to "ǖ", "ü2" to "ǘ", "ü3" to "ǚ", "ü4" to "ǜ"
    )

    fun toPinyin(input: String): String {
        val toneMarks = tonemap
        var result= input
        for ((key, value) in toneMarks) {
            result = result.replace(key, value)
        }
        return result

    }

}