package com.example.gengolearning.model.appmodels

import kotlinx.serialization.Serializable

@Serializable
data class GeminaiGrammarResponse(
    val explanation: String,
    val example: String
)
