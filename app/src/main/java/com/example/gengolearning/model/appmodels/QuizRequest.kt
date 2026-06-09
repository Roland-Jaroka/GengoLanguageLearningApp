package com.example.gengolearning.model.appmodels

import kotlinx.serialization.Serializable

@Serializable
data class QuizRequest(
    val language: String,
    val level: String
)
