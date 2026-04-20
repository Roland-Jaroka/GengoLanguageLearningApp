package com.example.gengolearning.ui.features.dashboard.learning.grammarDetails

data class EditGrammarState(
    val showDialog: Boolean = false,
    val title: String = "",
    val summary: String = "",
    val titleFieldValidation: Boolean = false,
    val summaryFieldValidation: Boolean = false,
    val titleFieldValidationMessage: Int? = null,
    val summaryFieldValidationMessage: Int? = null
)

