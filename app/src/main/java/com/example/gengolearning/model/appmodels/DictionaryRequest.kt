package com.example.gengolearning.model.appmodels

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryRequest(
    val word: String
)
