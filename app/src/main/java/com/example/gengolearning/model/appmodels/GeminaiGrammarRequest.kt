package com.example.gengolearning.model.appmodels

import kotlinx.serialization.Serializable

@Serializable
data class GeminaiGrammarRequest(
    val language: String,
    val grammarTopic: String
)
