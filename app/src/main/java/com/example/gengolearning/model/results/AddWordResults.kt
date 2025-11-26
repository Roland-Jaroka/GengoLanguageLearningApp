package com.example.gengolearning.model.results

sealed class AddWordResults {
    object Success: AddWordResults()
    object BlankWord: AddWordResults()
    object BlankTranslation: AddWordResults()
}