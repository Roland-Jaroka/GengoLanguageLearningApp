package com.example.gengolearning.model

data class Grammar(
    val grammar: String,
    val explanation: String,
    val examples: List<String>? = null,
    val id: String,
    val title: String? = null,
)
