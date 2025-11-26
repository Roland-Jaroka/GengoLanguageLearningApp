package com.example.gengolearning.model.results

sealed class AddGrammarResults {
    object Success: AddGrammarResults()
    object BlankGrammar: AddGrammarResults()
    object BlankExplanation: AddGrammarResults()

}