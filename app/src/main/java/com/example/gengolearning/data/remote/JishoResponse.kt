package com.example.gengolearning.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JishoResponse(
    val meta: Meta,
    val data: List<JishoEntry>
)

@Serializable
data class Meta(
    val status: Int
)
@Serializable
data class JishoEntry(
    val slug : String,
    @SerialName("is_common")
    val isCommon: Boolean? = null,
    val tags: List<String>,
    val jlpt: List<String> = emptyList(),
    val japanese: List<JapaneseWords>,
    val senses: List<Sense>

    )

@Serializable
data class JapaneseWords(
    val word: String="",
    val reading: String=""
)

@Serializable
data class Sense(
    @SerialName("english_definitions")
    val englishDefinitions: List<String> = emptyList()
)


